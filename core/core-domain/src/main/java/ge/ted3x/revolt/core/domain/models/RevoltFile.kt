package ge.ted3x.revolt.core.domain.models

data class RevoltFile(
    val id: String,
    val tag: String,
    val filename: String,
    val metadata: RevoltMetadata,
    val contentType: String,
    val size: Int,
    val deleted: Boolean?,
    val reported: Boolean?,
    val messageId: String?,
    val userId: String?,
    val serverId: String?,
    val objectId: String?,
    val url: String
)
