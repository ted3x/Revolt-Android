package ge.ted3x.revolt.core.data

import app.revolt.RevoltApi
import app.revolt.model.user.RevoltUserApiModel
import app.revolt.model.user.request.RevoltUserChangeUsernameApiRequest
import app.revolt.model.user.request.RevoltUserEditApiRequest
import ge.ted3x.core.database.RevoltFileQueries
import ge.ted3x.core.database.RevoltUserQueries
import ge.ted3x.revolt.core.data.mapper.RevoltFileMapper
import ge.ted3x.revolt.core.data.mapper.RevoltUserMapper
import ge.ted3x.revolt.core.data.mapper.RevoltUserStatusMapper
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.core.RevoltFileDomain
import ge.ted3x.revolt.core.domain.models.RevoltUser
import ge.ted3x.revolt.core.domain.models.request.RevoltUserEditRequest
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
                    avatarBaseUrl = avatarBaseUrl,
                    backgroundBaseUrl = backgroundBaseUrl,
                    userEntity = userEntity,
                    avatarEntity = userEntity.avatar_id?.let { fileQueries.getFile(it) },
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
        return updateAndGetUser(user = user, isCurrentUser = true)
    }

    override suspend fun getUser(userId: String): RevoltUser {
        val user = revoltApi.users.fetchUser(userId)
        return updateAndGetUser(user = user, isCurrentUser = false)
    }

    override suspend fun editUser(request: RevoltUserEditRequest) {
        val currentUserId = userQueries.getCurrentUser().id
        val removeListApi = request.remove?.map {
            when(it) {
                RevoltUserEditRequest.FieldsUser.Avatar -> RevoltUserEditApiRequest.FieldsUser.Avatar
                RevoltUserEditRequest.FieldsUser.StatusText -> RevoltUserEditApiRequest.FieldsUser.StatusText
                RevoltUserEditRequest.FieldsUser.StatusPresence -> RevoltUserEditApiRequest.FieldsUser.StatusPresence
                RevoltUserEditRequest.FieldsUser.ProfileContent -> RevoltUserEditApiRequest.FieldsUser.ProfileContent
                RevoltUserEditRequest.FieldsUser.ProfileBackground -> RevoltUserEditApiRequest.FieldsUser.ProfileBackground
                RevoltUserEditRequest.FieldsUser.DisplayName -> RevoltUserEditApiRequest.FieldsUser.DisplayName
            }
        }
        revoltApi.users.editUser(
            currentUserId, with(request) {
                RevoltUserEditApiRequest(
                    displayName,
                    avatarId,
                    request.status?.let { userStatusMapper.mapDomainToApi(it) },
                    RevoltUserEditApiRequest.Profile(request.profile?.content, request.profile?.background),
                    badges,
                    flags,
                    removeListApi
                )
            }
        )
        // Current does not return anything because API does not fetch updated profile with UserEntity
        // return updateAndGetUser(response, true)
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
        val userId = user.id
        userQueries.insertUser(userMapper.mapApiToEntity(user, isCurrentUser).userEntity)
        user.avatar?.let { avatar ->
            val fileEntity = fileMapper.mapApiToEntity(avatar)
            fileQueries.insertFile(fileEntity)
        }
        user.profile?.background?.let { background ->
            val fileEntity = fileMapper.mapApiToEntity(background)
            fileQueries.insertFile(fileEntity)
        }
        val userEntity = userQueries.getUser(userId)
        val avatarBaseUrl = configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Avatar)
        val backgroundBaseUrl =
            configurationRepository.getFileUrlWithDomain(RevoltFileDomain.Background)
        return userMapper.mapEntityToDomain(
            avatarBaseUrl = avatarBaseUrl,
            backgroundBaseUrl = backgroundBaseUrl,
            userEntity = userEntity,
            avatarEntity = userEntity.avatar_id?.let { fileQueries.getFile(it) },
            backgroundEntity = userEntity.profile_background_id?.let { fileQueries.getFile(it) },
            relationsEntity = userQueries.getRelations(userId)
        )
    }
}