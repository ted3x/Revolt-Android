package ge.ted3x.revolt.core.domain.models.user

import ge.ted3x.revolt.core.domain.models.general.RevoltFile

data class RevoltUserProfile(
    val content: String?,
    val background: RevoltFile?
)