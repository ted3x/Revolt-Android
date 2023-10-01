package ge.ted3x.revolt.core.domain.models.server

import ge.ted3x.revolt.core.domain.models.RevoltFile

data class RevoltServerMember(
    val userId: String,
    val serverId: String,
    val joinedAt: String,
    val nickname: String? = null,
    val avatar: RevoltFile? = null,
    val roles: List<String>? = null,
    val timeout: String? = null
)
