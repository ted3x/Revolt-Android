package ge.ted3x.revolt.core.domain.models.session

sealed interface RevoltLoginResponse {

    data class Success(
        val id: String,
        val userId: String,
        val token: String,
        val name: String,
        val subscription: Subscription? = null
    ) : RevoltLoginResponse {

        data class Subscription(
            val endpoint: String,
            val p256dh: String,
            val auth: String
        )
    }

    data class MFA(
        val ticket: String,
        val allowedMethods: RevoltMFAMethod
    ) : RevoltLoginResponse

    data class Disabled(
        val userId: String
    ) : RevoltLoginResponse

}