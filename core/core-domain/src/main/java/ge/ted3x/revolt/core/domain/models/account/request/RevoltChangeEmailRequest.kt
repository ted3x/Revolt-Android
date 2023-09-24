package ge.ted3x.revolt.core.domain.models.account.request

data class RevoltChangeEmailRequest(
    val email: String,
    val currentPassword: String
)