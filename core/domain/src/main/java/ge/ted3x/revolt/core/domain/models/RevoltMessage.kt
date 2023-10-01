package ge.ted3x.revolt.core.domain.models

data class RevoltMessage(
    val id: String,
    val nonce: String? = null,
    val channel: String,
    val author: Author,
    val webhook: Webhook? = null,
    val content: String? = null,
    val system: System? = null,
    val attachments: List<RevoltFile>? = null,
    val edited: String? = null,
    val embeds: List<RevoltEmbed>? = null,
    val mentions: List<Mention>? = null,
    val replies: List<Reply>? = null,
    val reactions: Map<String, List<String>>? = null,
    val interactions: Interactions? = null,
    val masquerade: RevoltMasquerade? = null
) {

    data class Author(
        val id: String,
        val username: String,
        val avatar: RevoltFile? = null
    )

    data class Mention(
        val userId: String,
        val username: String,
        val avatar: RevoltFile?
    )

    data class Reply(
        val userId: String,
        val username: String,
        val avatarUrl: RevoltFile?,
        val content: String
    )

    data class Webhook(
        val name: String,
        val avatar: String? = null
    )

    sealed interface System {

        data class Text(val content: String) : System

        data class UserAdded(val id: String, val by: String) : System

        data class UserRemoved(val id: String, val by: String) : System

        data class UserJoined(val id: String) : System

        data class UserLeft(val id: String) : System

        data class UserKicked(val id: String) : System

        data class UserBanned(val id: String) : System

        data class ChannelRenamed(val name: String, val by: String) : System

        data class ChannelDescriptionChanged(val name: String, val by: String) : System

        data class ChannelIconChanged(val name: String, val by: String) : System

        data class ChannelOwnershipChanged(val from: String, val to: String) : System
    }

    data class Interactions(
        val reactions: List<String>?,
        val restrictReactions: Boolean? = null
    )
}
