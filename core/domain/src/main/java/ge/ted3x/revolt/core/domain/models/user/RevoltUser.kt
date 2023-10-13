package ge.ted3x.revolt.core.domain.models.user

import ge.ted3x.revolt.core.domain.models.general.RevoltFile

data class RevoltUser(
    val id: String,
    val username: String,
    val discriminator: String,
    val displayName: String,
    val avatar: RevoltFile?,
    val relations: List<Relationship>?,
    val badges: Int?,
    val status: RevoltUserStatus?,
    val profile: RevoltUserProfile?,
    val flags: Int?,
    val privileged: Boolean?,
    val bot: Bot?,
    val relationship: RevoltRelationshipStatus?,
    val online: Boolean?
) {
    data class Relationship(
        val id: String,
        val status: RevoltRelationshipStatus
    )

    data class Bot(val owner: String)

    companion object {
        val Empty = RevoltUser(
            id = "",
            username = "",
            discriminator = "",
            displayName = "",
            avatar = null,
            relations = null,
            badges = null,
            status = null,
            profile = null,
            flags = null,
            privileged = false,
            bot = null,
            relationship = null,
            online = false
        )
    }
}