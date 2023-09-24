package ge.ted3x.revolt.core.domain.models.account.request

data class RevoltAccountCreateRequest(
    val email: String,
    val password: String,
    val invite: String?,
    val captcha: String?
)