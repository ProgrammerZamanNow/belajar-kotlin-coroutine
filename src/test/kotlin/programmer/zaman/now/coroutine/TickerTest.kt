package programmer.zaman.now.coroutine

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*

class TickerTest {

    @Test
    fun testTicker() {
        val receiveChannel = ticker(delayMillis = 1000)
        runBlocking {
            val job = launch {
                repeat(10) {
                    receiveChannel.receive()
                    println(Date())
                }
            }
            job.join()
        }
    }

    @Test
    fun testTimer() {
        val receiveChannel = GlobalScope.produce<String?> {
            while (true) {
                delay(1000)
                send(null)
            }
        }
        runBlocking {
            val job = launch {
                repeat(10) {
                    receiveChannel.receive()
                    println(Date())
                }
            }
            job.join()
        }
    }
}