package programmer.zaman.now.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ActorTest {

    @Test
    fun testActor() {
        val scope = CoroutineScope(Dispatchers.IO)

        val sendChannel = scope.actor<Int>(capacity = 10) {
            repeat(10) {
                println("Actor receive data ${receive()}")
            }
        }

        val job = scope.launch {
            repeat(10) {
                sendChannel.send(it)
            }
        }

        runBlocking { job.join() }
    }
}