package ge.ted3x.revolt.core.domain.models.general

data class RevoltConfiguration(
    val revolt: String,
    val features: Features,
    val ws: String,
    val app: String,
    val vapid: String,
    val build: Build
) {

    data class Features(
        val captcha: HCaptchaConfiguration,
        val email: Boolean,
        val inviteOnly: Boolean,
        val autumn: GeneralServerConfiguration,
        val january: GeneralServerConfiguration,
        val voso: VoiceServerConfiguration
    ) {

        data class HCaptchaConfiguration(val enabled: Boolean, val key: String)
        data class GeneralServerConfiguration(val enabled: Boolean, val url: String)
        data class VoiceServerConfiguration(val enabled: Boolean, val url: String, val ws: String)
    }

    data class Build(
        val commitSHA: String,
        val commitTimestamp: String,
        val semver: String,
        val originUrl: String,
        val timestamp: String
    )
}