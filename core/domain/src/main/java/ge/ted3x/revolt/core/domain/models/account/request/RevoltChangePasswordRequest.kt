package ge.ted3x.revolt.core.domain.models.account.request

data class RevoltChangePasswordRequest(
    val password: String,
    val currentPassword: String
)