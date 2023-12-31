package ge.ted3x.core.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.paging3.QueryPagingSource
import ge.ted3x.core.database.models.MessageWithAttachments
import ge.ted3x.revolt.RevoltMessageEntity
import ge.ted3x.revolt.Messages
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.SelectMessageById
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RevoltMessageDao @Inject constructor(private val database: RevoltDatabase) {

    fun insertMessages(messages: List<RevoltMessageEntity>) {
        database.transaction {
            messages.forEach {
                database.revoltMessageQueries.insertMessage(it)
            }
        }
    }

    fun insertMessage(message: RevoltMessageEntity) {
        database.revoltMessageQueries.insertMessage(message)
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

    fun getMessage(id: String): SelectMessageById? {
        return database.revoltMessageQueries.selectMessageById(id).executeAsOneOrNull()
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