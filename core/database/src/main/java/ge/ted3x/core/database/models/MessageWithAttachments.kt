package ge.ted3x.core.database.models

import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.RevoltMessageEntity

data class MessageWithAttachments(val message: RevoltMessageEntity, val attachments: List<RevoltFileEntity>)
