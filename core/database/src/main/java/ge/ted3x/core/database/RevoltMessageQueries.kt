package ge.ted3x.core.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ge.ted3x.core.database.models.MessageWithAttachments
import ge.ted3x.revolt.MessageEntity
import ge.ted3x.revolt.RevoltDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RevoltMessageQueries @Inject constructor(private val database: RevoltDatabase) {

    fun insertMessages(messages: List<MessageEntity>) {
        database.transaction {
            messages.forEach {
                database.revoltMessageQueries.insertMessages(it)
            }
        }
    }

    fun messagesFlow(channel: String): Flow<List<MessageWithAttachments>> {
        return database.revoltMessageQueries.selectMessagesByChannel(channel).asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { messageEntity ->
                    val attachments = database.revoltFileQueries.selectFileById(messageEntity.id).executeAsList()
                    MessageWithAttachments(messageEntity, attachments)
                }
            }
    }
}