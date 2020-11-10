package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test
import java.util.*

class FlowTest {

    @Test
    fun testFlow() {
        val flow1: Flow<Int> = flow {
            println("Start flow")
            repeat(100) {
                println("Emit $it")
                emit(it)
            }
        }

        runBlocking {
            flow1.collect {
                println("Receive $it")
            }
        }
    }

    suspend fun numberFlow(): Flow<Int> = flow {
        repeat(100) {
            emit(it)
        }
    }

    suspend fun changeToString(number: Int): String {
        delay(100)
        return "Number $number"
    }

    @Test
    fun testFlowOperator() {
        runBlocking {
            val flow1 = numberFlow()
            flow1.filter { it % 2 == 0 }
                    .map { changeToString(it) }
                    .collect { println(it) }
        }
    }

    @Test
    fun testFlowException() {
        runBlocking {
            numberFlow()
                    .map { check(it < 10); it }
                    .onEach { println(it) }
                    .catch { println("Error ${it.message}") }
                    .onCompletion { println("Done") }
                    .collect()
        }
    }

    @Test
    fun testCancellableFlow() {
        val scope = CoroutineScope(Dispatchers.IO)
        runBlocking {
            val job = scope.launch {
                numberFlow()
                        .onEach {
                            if (it > 10) cancel()
                            else println(it)
                        }
                        .collect()
            }
            job.join()
        }
    }

    @Test
    fun testSharedFlow() {
        val scope = CoroutineScope(Dispatchers.IO)
        val sharedFlow = MutableSharedFlow<Int>()

        scope.launch {
            repeat(10) {
                println("   Send     1 : $it : ${Date()}")
                sharedFlow.emit(it)
                delay(1000)
            }
        }

        scope.launch {
            sharedFlow.asSharedFlow()
                    .buffer(10)
                    .map { "Receive Job 1 : $it : ${Date()}" }
                    .collect {
                        delay(1000)
                        println(it)
                    }
        }

        scope.launch {
            sharedFlow.asSharedFlow()
                    .buffer(10)
                    .map { "Receive Job 2 : $it : ${Date()}" }
                    .collect {
                        delay(2000)
                        println(it)
                    }
        }

        runBlocking {
            delay(22_000)
            scope.cancel()
        }
    }

    @Test
    fun testStateFlow() {
        val scope = CoroutineScope(Dispatchers.IO)
        val stateFlow = MutableStateFlow(0)

        scope.launch {
            repeat(10) {
                println("   Send     1 : $it : ${Date()}")
                stateFlow.emit(it)
                delay(1000)
            }
        }

        scope.launch {
            stateFlow.asStateFlow()
                    .map { "Receive Job 2 : $it : ${Date()}" }
                    .collect {
                        println(it)
                        delay(5000)
                    }
        }

        runBlocking {
            delay(22_000)
            scope.cancel()
        }
    }
}