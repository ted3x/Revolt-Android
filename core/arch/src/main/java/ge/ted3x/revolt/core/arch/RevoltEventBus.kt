package ge.ted3x.revolt.core.arch

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RevoltEventBus @Inject constructor() {

    val channel = Channel<RevoltEvent>()

    fun sendEvent(event: RevoltEvent) {
        channel.trySend(event)
    }

    suspend inline fun <reified Event: RevoltEvent> listen(crossinline onReceive: suspend (Event) -> Unit) {
        channel.consumeAsFlow().filter { it is Event }.collect {
            onReceive.invoke(it as Event)
        }
    }
}