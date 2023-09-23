package ge.ted3x.revolt.core.data.mapper

import android.content.res.Resources.NotFoundException
import app.revolt.model.general.RevoltFileApiModel
import app.revolt.model.general.RevoltMetadataApiModel
import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.core.domain.models.RevoltFile
import ge.ted3x.revolt.core.domain.models.RevoltMetadata
import javax.inject.Inject

class RevoltFileMapper @Inject constructor(){

    fun mapEntityToDomain(entityModel: FileEntity): RevoltFile {
        return with(entityModel) {
            RevoltFile(
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
                objectId = object_id
            )
        }
    }

    fun mapApiToEntity(apiModel: RevoltFileApiModel): FileEntity {
        return with(apiModel) {
            val metadataEntity = apiModel.metadata.toEntity()
            FileEntity(
                id = id,
                tag = tag,
                filename = filename,
                metadata_type = metadataEntity.type,
                metadata_height = metadataEntity.height,
                metadata_width = metadataEntity.width,
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
    private fun RevoltMetadataApiModel.toEntity(): RevoltMetadataEntity {
        return when (this) {
            RevoltMetadataApiModel.Audio -> RevoltMetadataEntity(type = "Audio")
            RevoltMetadataApiModel.File -> RevoltMetadataEntity(type = "File")
            RevoltMetadataApiModel.Text -> RevoltMetadataEntity(type = "Text")
            is RevoltMetadataApiModel.Image -> RevoltMetadataEntity(
                type = "Image",
                height = height,
                width = width
            )

            is RevoltMetadataApiModel.Video -> RevoltMetadataEntity(
                type = "Video",
                height = height,
                width = width
            )
        }
    }

    private fun FileEntity.toMetadataDomain(): RevoltMetadata {
        return when (metadata_type) {
            "Audio" -> RevoltMetadata.Audio
            "File" -> RevoltMetadata.File
            "Text" -> RevoltMetadata.Text
            "Image" -> RevoltMetadata.Image(metadata_height!!, metadata_width!!)
            "Video" -> RevoltMetadata.Video(metadata_height!!, metadata_width!!)
            else -> throw NotFoundException(metadata_type)
        }
    }

    data class RevoltMetadataEntity(
        val type: String,
        val height: Int? = null,
        val width: Int? = null
    )
}