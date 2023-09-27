package ge.ted3x.revolt.core.domain.models.request

import ge.ted3x.revolt.core.domain.models.RevoltUserStatus

data class RevoltUserEditRequest(
    val displayName: String? = null,
    val avatarId: String? = null,
    val status: RevoltUserStatus? = null,
    val profile: Profile? = null,
    val badges: Int? = null,
    val flags: Int? = null,
    val remove: List<FieldsUser>? = null
) {

    data class Profile(val content: String? = null, val background: String? = null)

    enum class FieldsUser {
        Avatar,
        StatusText,
        StatusPresence,
        ProfileContent,
        ProfileBackground,
        DisplayName
    }
}
