package ge.ted3x.revolt.core.domain.repository.user

import ge.ted3x.revolt.core.domain.models.user.RevoltUser
import ge.ted3x.revolt.core.domain.models.user.request.RevoltUserEditRequest
import kotlinx.coroutines.flow.Flow

interface RevoltUserRepository {

    suspend fun observeSelf(): Flow<RevoltUser>
    suspend fun getSelf(): RevoltUser
    suspend fun getUser(userId: String): RevoltUser
    suspend fun editUser(request: RevoltUserEditRequest)
    suspend fun saveUser(user: RevoltUser, isCurrentUser: Boolean? = null)
    suspend fun fetchUserFlags()
    suspend fun changeUsername(username: String, password: String)
    suspend fun fetchDefaultAvatar()
    suspend fun fetchUserProfile()
}