package ge.ted3x.revolt.core.data.mapper.general

import android.content.res.Resources.NotFoundException
import app.revolt.model.general.RevoltFileApiModel
import app.revolt.model.general.RevoltMetadataApiModel
import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.core.domain.models.general.RevoltFile
import ge.ted3x.revolt.core.domain.models.general.RevoltMetadata
import javax.inject.Inject

class RevoltFileMapper @Inject constructor(){

    // API To Domain
    fun mapApiToDomain(apiModel: RevoltFileApiModel, baseUrl: String?): RevoltFile {
        return with(apiModel) {
            RevoltFile(
                id = id,
                tag = tag,
                filename = filename,
                metadata = metadata.toDomain(),
                contentType = contentType,
                size = size,
                deleted = deleted,
                reported = reported,
                messageId = messageId,
                userId = userId,
                serverId = serverId,
                objectId = objectId,
                url = "$baseUrl$id"
            )
        }
    }

    private fun RevoltMetadataApiModel.toDomain(): RevoltMetadata {
        return when(this) {
            RevoltMetadataApiModel.Audio -> RevoltMetadata.Audio
            RevoltMetadataApiModel.File -> RevoltMetadata.File
            is RevoltMetadataApiModel.Image -> RevoltMetadata.Image(width = width, height = height)
            RevoltMetadataApiModel.Text -> RevoltMetadata.Text
            is RevoltMetadataApiModel.Video -> RevoltMetadata.Video(width = width, height = height)
        }
    }

    // Domain To Entity
    fun mapDomainToEntity(domainModel: RevoltFile): RevoltFileEntity {
        with(domainModel) {
            val entityMetadata = metadata.toEntityMetadata()
            return RevoltFileEntity(
                id = id,
                tag = tag,
                filename = filename,
                metadata_type = entityMetadata.first,
                metadata_width = entityMetadata.second,
                metadata_height = entityMetadata.third,
                content_type = contentType,
                size = size,
                deleted = deleted,
                reported = reported,
                message_id = messageId,
                user_id = userId,
                server_id = serverId,
                object_id = objectId
            )
        }
    }

    private fun RevoltMetadata.toEntityMetadata(): Triple<String, Int?, Int?> {
        return when(this) {
            RevoltMetadata.Audio -> Triple("Audio", null, null)
            RevoltMetadata.File -> Triple("File", null, null)
            RevoltMetadata.Text -> Triple("Text", null, null)
            is RevoltMetadata.Image -> Triple("Image", width, height)
            is RevoltMetadata.Video -> Triple("Video", width, height)
        }
    }

    // Entity To Domain
    fun mapEntityToDomain(entityModel: RevoltFileEntity, url: String): RevoltFile {
        with(entityModel) {
            return RevoltFile(
                id = id,
                tag = tag,
                filename = filename,
                metadata = toMetadataDomain(),
                contentType = content_type,
                size = size,
                deleted = deleted,
                reported = reported,
                messageId = message_id,
                userId = user_id,
                serverId = server_id,
                objectId = object_id,
                url = "$url/$id"
            )
        }
    }

    private fun RevoltFileEntity.toMetadataDomain(): RevoltMetadata {
        return when (metadata_type) {
            "Audio" -> RevoltMetadata.Audio
            "File" -> RevoltMetadata.File
            "Text" -> RevoltMetadata.Text
            "Image" -> RevoltMetadata.Image(metadata_height!!, metadata_width!!)
            "Video" -> RevoltMetadata.Video(metadata_height!!, metadata_width!!)
            else -> throw NotFoundException(metadata_type)
        }
    }

    fun mapApiToEntity(apiModel: RevoltFileApiModel): RevoltFileEntity {
        return mapDomainToEntity(mapApiToDomain(apiModel, null))
    }
}