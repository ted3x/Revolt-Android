package ge.ted3x.revolt.core.domain.user

import ge.ted3x.revolt.core.domain.models.RevoltUser
import ge.ted3x.revolt.core.domain.models.request.RevoltUserEditRequest
import kotlinx.coroutines.flow.Flow

interface RevoltUserRepository {

    suspend fun observeSelf(): Flow<RevoltUser>
    suspend fun getSelf(): RevoltUser
    suspend fun getUser(userId: String): RevoltUser
    suspend fun editUser(request: RevoltUserEditRequest)
    suspend fun fetchUserFlags()
    suspend fun changeUsername(username: String, password: String)
    suspend fun fetchDefaultAvatar()
    suspend fun fetchUserProfile()
}