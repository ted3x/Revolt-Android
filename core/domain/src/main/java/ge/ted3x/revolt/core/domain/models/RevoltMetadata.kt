package ge.ted3x.revolt.core.domain.models

sealed class RevoltMetadata {

    data object File: RevoltMetadata()

    data object Text: RevoltMetadata()

    data object Audio: RevoltMetadata()

    data class Image(val width: Int, val height: Int): RevoltMetadata()

    data class Video(val width: Int, val height: Int): RevoltMetadata()
}