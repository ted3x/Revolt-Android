package ge.ted3x.revolt.core.data

import app.revolt.RevoltApi
import app.revolt.model.user.RevoltUserApiModel
import ge.ted3x.core.database.RevoltFileQueries
import ge.ted3x.core.database.RevoltUserQueries
import ge.ted3x.revolt.core.data.mapper.RevoltFileMapper
import ge.ted3x.revolt.core.data.mapper.RevoltUserMapper
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.models.RevoltUser
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class RevoltUserRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationRepository: RevoltConfigurationRepository,
    private val userQueries: RevoltUserQueries,
    private val fileQueries: RevoltFileQueries,
    private val fileMapper: RevoltFileMapper,
    private val userMapper: RevoltUserMapper
) : RevoltUserRepository {

    override suspend fun observeSelf(): Flow<RevoltUser> {
        val avatarBaseUrl = configurationRepository.getAvatarBaseUrl()
        return userQueries.getUserObservable()
            .mapLatest { userEntity ->
                val profile = userQueries.getProfile(userEntity.id)
                userMapper.mapEntityToDomain(
                    autumnUrl = avatarBaseUrl,
                    userEntity = userEntity,
                    avatarEntity = userEntity.avatar_id?.let { fileQueries.getFile(it) },
                    profileEntity = profile,
                    backgroundEntity = profile?.background_id?.let { fileQueries.getFile(it) },
                    relationsEntity = userQueries.getRelations(userEntity.id),
                    botEntity = userQueries.getBot(userEntity.id)
                )
            }
    }

    override suspend fun getSelf(): RevoltUser {
        val user = revoltApi.users.fetchSelf()
        return updateAndGetUser(user = user, isCurrentUser = true)
    }

    override suspend fun getUser(userId: String): RevoltUser {
        val user = revoltApi.users.fetchUser(userId)
        return updateAndGetUser(user = user, isCurrentUser = false)
    }

    override suspend fun editUser() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserFlags() {
        TODO("Not yet implemented")
    }

    override suspend fun changeUsername(userId: String, username: String) {
        userQueries.updateUsername(userId = userId, username = username)
    }

    override suspend fun fetchDefaultAvatar() {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUserProfile() {
        TODO("Not yet implemented")
    }

    private suspend fun updateAndGetUser(
        user: RevoltUserApiModel,
        isCurrentUser: Boolean
    ): RevoltUser {
        val userId = user.id
        userQueries.insertUser(userMapper.mapApiToEntity(user, isCurrentUser).userEntity)
        user.avatar?.let { avatar ->
            val fileEntity = fileMapper.mapApiToEntity(avatar)
            fileQueries.insertFile(fileEntity)
        }
        val userEntity = userQueries.getUser(userId)
        val profile = userQueries.getProfile(userId)
        return userMapper.mapEntityToDomain(
            autumnUrl = configurationRepository.getAvatarBaseUrl(),
            userEntity = userEntity,
            avatarEntity = userEntity.avatar_id?.let { fileQueries.getFile(it) },
            profileEntity = profile,
            backgroundEntity = profile?.background_id?.let { fileQueries.getFile(it) },
            relationsEntity = userQueries.getRelations(userId),
            botEntity = userQueries.getBot(userId)
        )
    }
}