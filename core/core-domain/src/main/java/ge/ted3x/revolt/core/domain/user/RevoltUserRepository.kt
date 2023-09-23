package ge.ted3x.revolt.core.domain.user

import ge.ted3x.revolt.core.domain.models.RevoltUser

interface RevoltUserRepository {

    suspend fun getSelf(): RevoltUser
    suspend fun getUser(userId: String): RevoltUser
    suspend fun editUser()
    suspend fun fetchUserFlags()
    suspend fun changeUsername()
    suspend fun fetchDefaultAvatar()
    suspend fun fetchUserProfile()
}