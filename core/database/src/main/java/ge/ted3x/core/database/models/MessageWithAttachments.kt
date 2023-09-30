package ge.ted3x.core.database.models

import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.MessageEntity

data class MessageWithAttachments(val message: MessageEntity, val attachments: List<FileEntity>)
