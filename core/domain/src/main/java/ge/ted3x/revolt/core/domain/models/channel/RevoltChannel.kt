package ge.ted3x.revolt.core.domain.models.channel

import ge.ted3x.revolt.core.domain.models.general.RevoltFile
import ge.ted3x.revolt.core.domain.models.general.RevoltOverrideField

sealed interface RevoltChannel {

    val id: String
    val name: String
    val iconUrl: String?

    data class SavedMessages(
        override val id: String,
        override val name: String = "",
        override val iconUrl: String = "",
        val user: String,
    ): RevoltChannel

    data class DirectMessage(
        override val id: String,
        override val name: String = "",
        override val iconUrl: String = "",
        val active: Boolean,
        val recipients: List<String>,
        val lastMessageId: String? = null
    ): RevoltChannel

    data class Group(
        override val id: String,
        override val name: String,
        val icon: RevoltFile? = null,
        override val iconUrl: String? = icon?.url,
        val owner: String,
        val description: String? = null,
        val recipients: List<String>,
        val lastMessageId: String? = null,
        val permissions: Int? = null,
        val nsfw: Boolean? = null
    ): RevoltChannel

    data class TextChannel(
        override val id: String,
        override val name: String,
        val icon: RevoltFile? = null,
        override val iconUrl: String? = icon?.url,
        val server: String,
        val description: String? = null,
        val lastMessageId: String? = null,
        val defaultPermissions: RevoltOverrideField? = null,
        val rolePermissions: Map<String, RevoltOverrideField>? = null,
        val nsfw: Boolean? = null
    ): RevoltChannel

    data class VoiceChannel(
        override val id: String,
        override val name: String,
        val icon: RevoltFile? = null,
        override val iconUrl: String? = icon?.url,
        val server: String,
        val description: String? = null,
        val defaultPermissions: RevoltOverrideField? = null,
        val rolePermissions: Map<String, RevoltOverrideField>? = null,
        val nsfw: Boolean? = null
    ): RevoltChannel
}

