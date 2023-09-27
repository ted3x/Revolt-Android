package ge.ted3x.revolt.core.domain.models.core

import ge.ted3x.revolt.core.domain.core.RevoltFileDomain

data class RevoltFileUploadRequest(
    val fileName: String,
    val bytes: ByteArray,
    val contentType: ContentType,
    val domain: RevoltFileDomain
) {
    enum class ContentType {
        JPEG,
        PNG,
        GIF
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RevoltFileUploadRequest

        if (fileName != other.fileName) return false
        if (!bytes.contentEquals(other.bytes)) return false
        if (contentType != other.contentType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + bytes.contentHashCode()
        result = 31 * result + contentType.hashCode()
        return result
    }
}