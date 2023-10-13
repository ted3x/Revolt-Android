package ge.ted3x.revolt.core.data.repository.user

import app.revolt.RevoltApi
import app.revolt.model.user.RevoltUserApiModel
import app.revolt.model.user.request.RevoltUserChangeUsernameApiRequest
import app.revolt.model.user.request.RevoltUserEditApiRequest
import ge.ted3x.core.database.dao.RevoltFileDao
import ge.ted3x.core.database.dao.RevoltUserDao
import ge.ted3x.revolt.core.data.mapper.general.RevoltFileMapper
import ge.ted3x.revolt.core.data.mapper.user.RevoltUserMapper
import ge.ted3x.revolt.core.data.mapper.user.RevoltUserStatusMapper
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.user.RevoltUser
import ge.ted3x.revolt.core.domain.models.user.request.RevoltUserEditRequest
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class RevoltUserRepositoryImpl @Inject constructor(
    private val revoltApi: RevoltApi,
    private val configurationRepository: RevoltConfigurationRepository,
    private val userQueries: RevoltUserDao,
    private val fileQueries: RevoltFileDao,
    private val fileMapper: RevoltFileMapper,
    private val userMapper: RevoltUserMapper,
    private val userStatusMapper: RevoltUserStatusMapper
) : RevoltUserRepository {

    override suspend fun observeSelf(): Flow<RevoltUser> {
        val avatarBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Avatar)
        val backgroundBaseUrl =
            configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Background)
        return userQueries.getUserObservable()
            .mapLatest { userEntity ->
                userMapper.mapEntityToDomain(
                    entityModel = userEntity,
                    avatarEntity = userEntity.avatar_id?.let { fileQueries.getFile(it) },
                    avatarBaseUrl = avatarBaseUrl,
                    backgroundBaseUrl = backgroundBaseUrl,
                    backgroundEntity = userEntity.profile_background_id?.let {
                        fileQueries.getFile(
                            it
                        )
                    },
                    relationsEntity = userQueries.getRelations(userEntity.id),
                )
            }
    }

    override suspend fun getSelf(): RevoltUser {
        val user = revoltApi.users.fetchSelf()
        return updateAndGetUser(user, true)
    }

    override suspend fun getUser(userId: String): RevoltUser {
        val user = revoltApi.users.fetchUser(userId)
        return updateAndGetUser(user, false)
    }

    override suspend fun editUser(request: RevoltUserEditRequest) {
        val removeListApi = request.remove?.map {
            when (it) {
                RevoltUserEditRequest.FieldsUser.Avatar -> RevoltUserEditApiRequest.FieldsUser.Avatar
                RevoltUserEditRequest.FieldsUser.StatusText -> RevoltUserEditApiRequest.FieldsUser.StatusText
                RevoltUserEditRequest.FieldsUser.StatusPresence -> RevoltUserEditApiRequest.FieldsUser.StatusPresence
                RevoltUserEditRequest.FieldsUser.ProfileContent -> RevoltUserEditApiRequest.FieldsUser.ProfileContent
                RevoltUserEditRequest.FieldsUser.ProfileBackground -> RevoltUserEditApiRequest.FieldsUser.ProfileBackground
                RevoltUserEditRequest.FieldsUser.DisplayName -> RevoltUserEditApiRequest.FieldsUser.DisplayName
            }
        }
        revoltApi.users.editUser(with(request) {
            RevoltUserEditApiRequest(
                displayName = displayName,
                avatarId = avatarId,
                status = request.status?.let { userStatusMapper.mapDomainToApi(it) },
                profile = request.profile?.let {
                    RevoltUserEditApiRequest.Profile(
                        it.content,
                        it.background
                    )
                },
                badges = badges,
                flags = flags,
                remove = removeListApi
            )
        }
        )
        // Current does not return anything because API does not fetch updated profile with UserEntity
        // return updateAndGetUser(response, true)
    }

    override suspend fun saveUser(user: RevoltUser, isCurrentUser: Boolean?) {
        val isCurrentUser = isCurrentUser ?: (userQueries.getCurrentUserId() == user.id)
        val userEntityCollection = userMapper.mapDomainToEntity(user, isCurrentUser)
        userQueries.insertUser(userEntityCollection.userEntity)
        userEntityCollection.avatarEntity?.let { avatar ->
            fileQueries.insertFile(avatar)
        }
        userEntityCollection.backgroundEntity?.let { background ->
            fileQueries.insertFile(background)
        }
    }

    override suspend fun fetchUserFlags() {
        TODO("Not yet implemented")
    }

    override suspend fun changeUsername(username: String, password: String) {
        revoltApi.users.changeUsername(RevoltUserChangeUsernameApiRequest(username, password))
        userQueries.updateUsername(username)
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
        saveUser(userMapper.mapApiToDomain(user, null, null), isCurrentUser)
        val userId = user.id
        val userEntity = userQueries.getUser(userId)
        val avatarBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Avatar)
        val backgroundBaseUrl =
            configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Background)
        return userMapper.mapEntityToDomain(
            entityModel = userEntity,
            avatarEntity = userEntity.avatar_id?.let { fileQueries.getFile(it) },
            avatarBaseUrl = avatarBaseUrl,
            backgroundBaseUrl = backgroundBaseUrl,
            backgroundEntity = userEntity.profile_background_id?.let { fileQueries.getFile(it) },
            relationsEntity = userQueries.getRelations(userId)
        )
    }
}