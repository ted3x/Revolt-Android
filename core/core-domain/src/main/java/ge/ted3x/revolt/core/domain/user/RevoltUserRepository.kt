package ge.ted3x.revolt.core.domain.user

interface RevoltUserRepository {

    suspend fun getUser()
    suspend fun editUser()
    suspend fun fetchUserFlags()
    suspend fun changeUsername()
    suspend fun fetchDefaultAvatar()
    suspend fun fetchUserProfile()
}