package ge.ted3x.revolt.core.data.mapper.channel.messaging

import ge.ted3x.revolt.RevoltEmbedEntity
import ge.ted3x.revolt.core.domain.models.general.RevoltEmbed
import javax.inject.Inject

class RevoltEmbedMapper @Inject constructor() {

    fun mapEntityToDomain(entityModel: RevoltEmbedEntity): RevoltEmbed {
        return with(entityModel) {
            when (entityModel.type) {
                "Website" -> {
                    RevoltEmbed.Website(
                        url = url,
                        originalUrl = original_url,
                        title = title,
                        description = description,
                        siteName = site_name,
                        iconUrl = icon_url,
                        color = color,
                        image = RevoltEmbed.EmbedImage(
                            url = image_url ?: "",
                            width = image_width?.toInt() ?: 0,
                            height = image_height?.toInt() ?: 0,
                            size = RevoltEmbed.EmbedImage.ImageSize.valueOf(image_size ?: "Large")
                        ),
                        video = RevoltEmbed.EmbedVideo(
                            url = video_url ?: "",
                            width = video_width?.toInt() ?: 0,
                            height = video_height?.toInt() ?: 0
                        ),
                        special = special_type?.let {
                            when (it) {
                                "GIF" -> RevoltEmbed.Website.SpecialEmbed.GIF
                                "Youtube" -> RevoltEmbed.Website.SpecialEmbed.Youtube(id = special_type_id!!)
                                "Lightspeed" -> RevoltEmbed.Website.SpecialEmbed.Lightspeed(
                                    id = special_type_id!!,
                                    contentType = RevoltEmbed.Website.SpecialEmbed.Lightspeed.LightspeedType.valueOf(
                                        special_type_content_type!!
                                    )
                                )

                                "Twitch" -> RevoltEmbed.Website.SpecialEmbed.Twitch(
                                    id = special_type_id!!,
                                    contentType = RevoltEmbed.Website.SpecialEmbed.Twitch.TwitchType.valueOf(
                                        special_type_content_type!!
                                    )
                                )

                                "Spotify" -> RevoltEmbed.Website.SpecialEmbed.Spotify(
                                    id = special_type_id!!,
                                    contentType = special_type_content_type!!
                                )

                                "Soundcloud" -> RevoltEmbed.Website.SpecialEmbed.Soundcloud
                                "Bandcamp" -> RevoltEmbed.Website.SpecialEmbed.Bandcamp(
                                    id = special_type_id!!,
                                    contentType = RevoltEmbed.Website.SpecialEmbed.Bandcamp.BandcampType.valueOf(
                                        special_type_content_type!!
                                    )
                                )

                                "Streamable" -> RevoltEmbed.Website.SpecialEmbed.Streamable(id = special_type_id!!)
                                else -> RevoltEmbed.Website.SpecialEmbed.None
                            }
                        }
                    )
                }

                "EmbedImage" -> {
                    RevoltEmbed.EmbedImage(
                        url = url ?: "",
                        width = image_width?.toInt() ?: 0,
                        height = image_height?.toInt() ?: 0,
                        size = RevoltEmbed.EmbedImage.ImageSize.valueOf(image_size ?: "Large")
                    )
                }

                "EmbedVideo" -> {
                    RevoltEmbed.EmbedVideo(
                        url = video_url ?: "",
                        width = video_width?.toInt() ?: 0,
                        height = video_height?.toInt() ?: 0
                    )
                }

                "TextEmbed" -> {
                    RevoltEmbed.TextEmbed(
                        iconUrl = icon_url,
                        url = url,
                        title = title,
                        description = description,
                        color = color
                    )
                }

                else -> RevoltEmbed.None
            }
        }
    }

    fun mapDomainToEntity(messageId: String, domainModel: RevoltEmbed): RevoltEmbedEntity {
        return with(domainModel) {
            when (this) {
                is RevoltEmbed.None -> getEmptyEmbedEntity(messageId, "None")

                is RevoltEmbed.Website -> {
                    var timestamp: String? = null

                    val specialType = when (special) {
                        RevoltEmbed.Website.SpecialEmbed.GIF -> "GIF"
                        is RevoltEmbed.Website.SpecialEmbed.Youtube -> {
                            timestamp =
                                (special as RevoltEmbed.Website.SpecialEmbed.Youtube).timestamp
                            "Youtube"
                        }

                        is RevoltEmbed.Website.SpecialEmbed.Lightspeed -> "Lightspeed"
                        is RevoltEmbed.Website.SpecialEmbed.Twitch -> "Twitch"
                        is RevoltEmbed.Website.SpecialEmbed.Spotify -> "Spotify"
                        is RevoltEmbed.Website.SpecialEmbed.Soundcloud -> "Soundcloud"
                        is RevoltEmbed.Website.SpecialEmbed.Bandcamp -> "Bandcamp"
                        is RevoltEmbed.Website.SpecialEmbed.Streamable -> "Streamable"
                        else -> null
                    }
                    getEmptyEmbedEntity(messageId, "Website").copy(
                        url = url,
                        original_url = originalUrl,
                        special_type = specialType,
                        title = title,
                        description = description,
                        special_type_id = special?.toEntitySpecialTypeId(),
                        special_type_timestamp = timestamp,
                        special_type_content_type = special?.toEntityContentType(),
                        site_name = siteName,
                        icon_url = iconUrl,
                        color = color,
                        image_url = image?.url,
                        image_width = image?.width?.toLong(),
                        image_height = image?.height?.toLong(),
                        image_size = image?.size?.name,
                        video_url = video?.url,
                        video_width = video?.width?.toLong(),
                        video_height = video?.height?.toLong()
                    )
                }

                is RevoltEmbed.EmbedImage -> {
                    getEmptyEmbedEntity(messageId, "EmbedImage").copy(
                        url = url,
                        image_width = width.toLong(),
                        image_height = height.toLong(),
                        image_size = size.name
                    )
                }

                is RevoltEmbed.EmbedVideo -> {
                    getEmptyEmbedEntity(messageId, "EmbedVideo").copy(
                        video_url = url,
                        video_width = width.toLong(),
                        video_height = height.toLong()
                    )
                }

                is RevoltEmbed.TextEmbed -> {
                    getEmptyEmbedEntity(messageId, "TextEmbed").copy(
                        icon_url = iconUrl,
                        url = url,
                        title = title,
                        description = description,
                        color = color
                    )
                }
            }
        }
    }

    private fun RevoltEmbed.Website.SpecialEmbed.toEntityContentType(): String? {
        return when (this) {
            is RevoltEmbed.Website.SpecialEmbed.Bandcamp -> {
                when (this.contentType) {
                    RevoltEmbed.Website.SpecialEmbed.Bandcamp.BandcampType.Album -> "Album"
                    RevoltEmbed.Website.SpecialEmbed.Bandcamp.BandcampType.Track -> "Track"
                }
            }

            is RevoltEmbed.Website.SpecialEmbed.Lightspeed -> {
                when (this.contentType) {
                    RevoltEmbed.Website.SpecialEmbed.Lightspeed.LightspeedType.Channel -> "Channel"
                }
            }

            is RevoltEmbed.Website.SpecialEmbed.Spotify -> this.contentType
            is RevoltEmbed.Website.SpecialEmbed.Twitch -> {
                when (this.contentType) {
                    RevoltEmbed.Website.SpecialEmbed.Twitch.TwitchType.Channel -> "Channel"
                    RevoltEmbed.Website.SpecialEmbed.Twitch.TwitchType.Video -> "Video"
                    RevoltEmbed.Website.SpecialEmbed.Twitch.TwitchType.Clip -> "Clip"
                }
            }

            else -> null
        }
    }

    private fun RevoltEmbed.Website.SpecialEmbed.toEntitySpecialTypeId(): String? {
        return when (this) {
            is RevoltEmbed.Website.SpecialEmbed.Bandcamp -> this.id
            is RevoltEmbed.Website.SpecialEmbed.Lightspeed -> this.id
            is RevoltEmbed.Website.SpecialEmbed.Spotify -> this.id
            is RevoltEmbed.Website.SpecialEmbed.Streamable -> this.id
            is RevoltEmbed.Website.SpecialEmbed.Twitch -> this.id
            is RevoltEmbed.Website.SpecialEmbed.Youtube -> this.id
            else -> null
        }
    }

    private fun getEmptyEmbedEntity(messageId: String, type: String): RevoltEmbedEntity {
        return RevoltEmbedEntity(
            id = 0,
            message_id = messageId,
            type = type,
            url = null,
            original_url = null,
            special_type = null,
            special_type_id = null,
            special_type_timestamp = null,
            special_type_content_type = null,
            title = null,
            description = null,
            site_name = null,
            icon_url = null,
            color = null,
            image_url = null,
            image_width = null,
            image_height = null,
            image_size = null,
            video_url = null,
            video_width = null,
            video_height = null,
        )
    }
}