package ge.ted3x.revolt.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class RevoltBaseInteractor<Input, Output>(private val dispatchers: RevoltCoroutineDispatchers) {

    protected abstract suspend fun operation(input: Input): Output

    suspend fun execute(input: Input, dispatcher: CoroutineDispatcher = dispatchers.default): Output {
        return withContext(dispatcher) {
            operation(input)
        }
    }
}