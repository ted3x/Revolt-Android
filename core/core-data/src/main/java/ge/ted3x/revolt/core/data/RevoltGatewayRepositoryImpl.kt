package ge.ted3x.revolt.core.data

import android.util.Log
import app.revolt.RevoltApi
import app.revolt.websocket.client.RevoltClientApiEvent
import app.revolt.websocket.server.RevoltServerApiEvent
import ge.ted3x.core.database.RevoltFileQueries
import ge.ted3x.core.database.RevoltUserQueries
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.user.RevoltGatewayRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserTokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RevoltGatewayRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val userQueries: RevoltUserQueries,
    private val fileQueries: RevoltFileQueries,
    private val userRepository: RevoltUserRepository,
    private val tokenRepository: RevoltUserTokenRepository,
    private val configurationRepository: RevoltConfigurationRepository
) : RevoltGatewayRepository {
    override suspend fun initialize() {
        revoltApi.ws.initialize(configurationRepository.getConfiguration().ws)
        handleEvents()
        val token = tokenRepository.retrieveToken() ?: return
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
        val profileEntity = userQueries.getProfile(userId)
        var updatedUserEntity = userEntity.copy(
            username = updatedUser.username ?: userEntity.username,
            discriminator = updatedUser.discriminator ?: userEntity.discriminator,
            display_name = updatedUser.displayName ?: userEntity.display_name,
            avatar_id = updatedUser.avatar?.id ?: userEntity.avatar_id,
            badges = updatedUser.badges ?: userEntity.badges,
            status_text = updatedUser.status?.text ?: userEntity.status_text,
            status_presence = updatedUser.status?.presence?.name ?: userEntity.status_presence,
            flags = updatedUser.flags ?: userEntity.flags,
            privileged = updatedUser.privileged ?: userEntity.privileged,
            relationship = updatedUser.relationship?.name ?: userEntity.relationship,
            online = updatedUser.online ?: userEntity.online,
        )
        event.clear.forEach { field ->
            when (field) {
                RevoltServerApiEvent.UserUpdate.Field.ProfileContent -> {
                    userQueries.updateProfile(userId, null, profileEntity?.background_id)
                }
                RevoltServerApiEvent.UserUpdate.Field.ProfileBackground -> {
                    userQueries.updateProfile(userId, profileEntity?.content, null)
                }
                RevoltServerApiEvent.UserUpdate.Field.StatusText -> {
                    updatedUserEntity = updatedUserEntity.copy(status_presence = null)
                }
                RevoltServerApiEvent.UserUpdate.Field.Avatar -> {
                    userEntity.avatar_id?.let { fileQueries.deleteFile(it) }
                    updatedUserEntity = updatedUserEntity.copy(avatar_id = null)
                }
            }
        }
        userQueries.insertUser(updatedUserEntity)
    }
}