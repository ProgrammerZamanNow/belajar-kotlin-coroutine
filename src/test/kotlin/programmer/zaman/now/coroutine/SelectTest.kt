package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.selects.select
import org.junit.jupiter.api.Test

class SelectTest {

    @Test
    fun testSelectDeferred() {
        val scope = CoroutineScope(Dispatchers.IO)

        val deferred1 = scope.async {
            delay(1000)
            1000
        }

        val deferred2 = scope.async {
            delay(2000)
            2000
        }

        val deferred3 = scope.async {
            delay(500)
            500
        }

        val job = scope.launch {
            val win = select<String> {
                deferred1.onAwait { "Result $it" }
                deferred2.onAwait { "Result $it" }
                deferred3.onAwait { "Result $it" }
            }
            println("Win : $win")
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testSelectChannel() {
        val scope = CoroutineScope(Dispatchers.IO)

        val receiveChannel1 = scope.produce {
            delay(1000)
            send(1000)
        }

        val receiveChannel2 = scope.produce {
            delay(2000)
            send(2000)
        }

        val receiveChannel3 = scope.produce {
            delay(500)
            send(500)
        }

        val job = scope.launch {
            val win = select<String> {
                receiveChannel1.onReceive { "Result $it" }
                receiveChannel2.onReceive { "Result $it" }
                receiveChannel3.onReceive { "Result $it" }
            }
            println("Win : $win")
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testSelectChannelAndDeferred() {
        val scope = CoroutineScope(Dispatchers.IO)

        val receiveChannel1 = scope.produce {
            delay(100)
            send(100)
        }

        val deferred2 = scope.async {
            delay(2000)
            2000
        }

        val deferred3 = scope.async {
            delay(500)
            500
        }

        val job = scope.launch {
            val win = select<String> {
                receiveChannel1.onReceive { "Result $it" }
                deferred2.onAwait { "Result $it" }
                deferred3.onAwait { "Result $it" }
            }
            println("Win : $win")
        }

        runBlocking {
            job.join()
        }
    }
}