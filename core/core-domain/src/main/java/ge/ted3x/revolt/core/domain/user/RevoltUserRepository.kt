package ge.ted3x.revolt.core.domain.user

import ge.ted3x.revolt.core.domain.models.RevoltUser
import kotlinx.coroutines.flow.Flow

interface RevoltUserRepository {

    suspend fun observeSelf(): Flow<RevoltUser>
    suspend fun getSelf(): RevoltUser
    suspend fun getUser(userId: String): RevoltUser
    suspend fun editUser()
    suspend fun fetchUserFlags()
    suspend fun changeUsername(userId: String, username: String)
    suspend fun fetchDefaultAvatar()
    suspend fun fetchUserProfile()
}