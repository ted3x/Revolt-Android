package ge.ted3x.revolt.core.data.repository.general

import android.util.Log
import app.revolt.RevoltApi
import app.revolt.model.general.RevoltFileApiModel
import app.revolt.websocket.client.RevoltClientApiEvent
import app.revolt.websocket.server.RevoltServerApiEvent
import app.revolt.websocket.server.RevoltServerApiEvent.UserUpdate.Field.Avatar
import app.revolt.websocket.server.RevoltServerApiEvent.UserUpdate.Field.ProfileBackground
import app.revolt.websocket.server.RevoltServerApiEvent.UserUpdate.Field.ProfileContent
import app.revolt.websocket.server.RevoltServerApiEvent.UserUpdate.Field.StatusText
import ge.ted3x.core.database.dao.RevoltFileDao
import ge.ted3x.core.database.dao.RevoltUserDao
import ge.ted3x.revolt.core.data.mapper.general.RevoltFileMapper
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.repository.general.RevoltGatewayRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserTokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RevoltGatewayRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val userQueries: RevoltUserDao,
    private val fileQueries: RevoltFileDao,
    private val userRepository: RevoltUserRepository,
    private val tokenRepository: RevoltUserTokenRepository,
    private val configurationRepository: RevoltConfigurationRepository,
    private val fileMapper: RevoltFileMapper
) : RevoltGatewayRepository {
    override suspend fun initialize() {
        revoltApi.ws.initialize(configurationRepository.getConfiguration().ws)
        handleEvents()
        val token = tokenRepository.retrieveToken() ?: return
        revoltApi.updateToken(token)
        revoltApi.ws.sendEvent(RevoltClientApiEvent.Authenticate(token))
    }

    private fun handleEvents() {
        CoroutineScope(Dispatchers.Default).launch {
            revoltApi.ws.incomingEvents.consumeAsFlow().buffer(Channel.UNLIMITED).collect {
                when (it) {
                    RevoltServerApiEvent.Authenticated -> {
                        Log.d("Authenticated", it.toString())
                    }

                    is RevoltServerApiEvent.Bulk -> TODO()
                    is RevoltServerApiEvent.Error -> TODO()
                    is RevoltServerApiEvent.Pong -> TODO()
                    is RevoltServerApiEvent.UserUpdate -> {
                        updateUser(it)
                    }
                }
            }
        }
    }

    private suspend fun updateUser(event: RevoltServerApiEvent.UserUpdate) {
        val userId = event.id
        val updatedUser = event.data
        val userEntity = userQueries.getUserOrNull(userId) ?: return
        val updatedUserEntity = userEntity.copy(
            username = updatedUser.username ?: userEntity.username,
            discriminator = updatedUser.discriminator ?: userEntity.discriminator,
            display_name = updatedUser.displayName ?: userEntity.display_name,
            avatar_id = if (event.clear.contains(Avatar)) {
                userEntity.avatar_id?.let { fileQueries.deleteFile(it) }
                null
            } else updatedUser.avatar?.id ?: userEntity.avatar_id,
            badges = updatedUser.badges ?: userEntity.badges,
            status_text = if (event.clear.contains(StatusText)) null else updatedUser.status?.text
                ?: userEntity.status_text,
            status_presence = updatedUser.status?.presence?.name ?: userEntity.status_presence,
            profile_content = if (event.clear.contains(ProfileContent)) {
                null
            } else {
                updatedUser.profile?.content ?: userEntity.profile_content
            },
            profile_background_id = if (event.clear.contains(ProfileBackground)) {
                userEntity.profile_background_id?.let { fileQueries.deleteFile(it) }
                null
            } else {
                updatedUser.profile?.background?.id ?: userEntity.profile_background_id
            },
            flags = updatedUser.flags ?: userEntity.flags,
            privileged = updatedUser.privileged ?: userEntity.privileged,
            relationship = updatedUser.relationship?.name ?: userEntity.relationship,
            online = updatedUser.online ?: userEntity.online,
        )
        shouldInsertNewFile(userEntity.profile_background_id, updatedUser.profile?.background)
        shouldInsertNewFile(userEntity.avatar_id, updatedUser.avatar)
        userQueries.insertUser(updatedUserEntity)
    }

    private fun shouldInsertNewFile(oldValueId: String?, newValue: RevoltFileApiModel?) {
        if(newValue != null && oldValueId != newValue.id) {
            oldValueId?.let { fileQueries.deleteFile(it) }
            val fileEntity = fileMapper.mapApiToEntity(newValue)
            fileQueries.insertFile(fileEntity)
        }
    }
}