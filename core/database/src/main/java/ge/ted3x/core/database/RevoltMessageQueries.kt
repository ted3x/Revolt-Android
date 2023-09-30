package ge.ted3x.core.database

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import ge.ted3x.core.database.models.MessageWithAttachments
import ge.ted3x.revolt.MessageEntity
import ge.ted3x.revolt.Messages
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

    fun getMessages(channel: String): PagingSource<Int, Messages> {
        return QueryPagingSource(
            countQuery = database.revoltMessageQueries.countMessages(channel),
            transacter = database.revoltMessageQueries,
            context = Dispatchers.IO,
            queryProvider = { limit, offset ->
                database.revoltMessageQueries.messages(channel, limit, offset)
            }
        )
    }

    fun messagesFlow(channel: String): Flow<List<MessageWithAttachments>> {
        return database.revoltMessageQueries.selectMessagesByChannel(channel).asFlow()
            .mapToList(Dispatchers.IO)
            .map { list ->
                list.map { messageEntity ->
                    val attachments =
                        database.revoltFileQueries.selectFileById(messageEntity.id).executeAsList()
                    MessageWithAttachments(messageEntity, attachments)
                }
            }
    }
}