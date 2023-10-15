package ge.ted3x.revolt.core.domain.models.server

import ge.ted3x.revolt.core.domain.models.general.RevoltOverrideField

data class RevoltServerRole(
    val name: String,
    val permissions: RevoltOverrideField,
    val color: String? = null,
    val hoist: Boolean? = null,
    val rank: Int = 0
)
