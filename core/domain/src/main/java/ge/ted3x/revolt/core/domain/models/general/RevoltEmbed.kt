package ge.ted3x.revolt.core.domain.models.general

sealed class RevoltEmbed {

    data object None : RevoltEmbed()

    data class Website(
        val url: String? = null,
        val originalUrl: String? = null,
        val special: SpecialEmbed? = null,
        val title: String? = null,
        val description: String? = null,
        val image: EmbedImage? = null,
        val video: EmbedVideo? = null,
        val siteName: String? = null,
        val iconUrl: String? = null,
        val color: String? = null,
    ) : RevoltEmbed() {

        sealed class SpecialEmbed {

            data object None : SpecialEmbed()

            data object GIF : SpecialEmbed()

            data class Youtube(val id: String, val timestamp: String? = null) : SpecialEmbed()

            data class Lightspeed(val id: String, val contentType: LightspeedType) : SpecialEmbed() {
                enum class LightspeedType { Channel }
            }

            data class Twitch(val id: String, val contentType: TwitchType) : SpecialEmbed() {
                enum class TwitchType { Channel, Video, Clip }
            }

            data class Spotify(val id: String, val contentType: String) : SpecialEmbed()

            data object Soundcloud : SpecialEmbed()

            data class Bandcamp(val id: String, val contentType: BandcampType) : SpecialEmbed() {
                enum class BandcampType { Album, Track }
            }

            data class Streamable(val id: String) : SpecialEmbed()
        }
    }

    data class EmbedImage(val url: String, val width: Int, val height: Int, val size: ImageSize) : RevoltEmbed() {
        enum class ImageSize { Large, Preview }
    }

    data class EmbedVideo(val url: String, val width: Int, val height: Int) : RevoltEmbed()

    data class TextEmbed(
        val iconUrl: String? = null,
        val url: String? = null,
        val title: String? = null,
        val description: String? = null,
        val media: RevoltFile? = null,
        val color: String? = null
    ) : RevoltEmbed()
}
