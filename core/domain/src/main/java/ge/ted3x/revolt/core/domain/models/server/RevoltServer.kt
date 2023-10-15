package ge.ted3x.revolt.core.domain.models.server

import ge.ted3x.revolt.core.domain.models.general.RevoltFile

data class RevoltServer(
    val id: String,
    val owner: String,
    val name: String,
    val description: String? = null,
    val channels: List<String>,
    val categories: List<Category>? = null,
    val systemMessages: SystemMessages? = null,
    val roles: Map<String, RevoltServerRole>? = null,
    val defaultPermissions: Int? = null,
    val icon: RevoltFile? = null,
    val banner: RevoltFile? = null,
    val flags: Int? = null,
    val nsfw: Boolean? = null,
    val analytics: Boolean? = null,
    val discoverable: Boolean? = null
) {
    data class Category(
        val id: String,
        val title: String,
        val channels: List<String>
    )

    data class SystemMessages(
        val userJoined: String?,
        val userLeft: String?,
        val userKicked: String?,
        val userBanned: String?,
    )
}
