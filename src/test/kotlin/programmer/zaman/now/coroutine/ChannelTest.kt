package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import org.junit.jupiter.api.Test

class ChannelTest {

    @Test
    fun testChannel() {
        runBlocking {

            val channel = Channel<Int>()

            val job1 = launch {
                println("send data 1 to channel")
                channel.send(1)
                println("send data 2 to channel")
                channel.send(2)
            }

            val job2 = launch {
                println("receive data ${channel.receive()}")
                println("receive data ${channel.receive()}")
            }

            joinAll(job1, job2)
            channel.close()

        }
    }

    @Test
    fun testChannelUnlimited() {
        runBlocking {

            val channel = Channel<Int>(capacity = Channel.UNLIMITED)

            val job1 = launch {
                println("send data 1 to channel")
                channel.send(1)
                println("send data 2 to channel")
                channel.send(2)
            }

            val job2 = launch {
                println("receive data ${channel.receive()}")
                println("receive data ${channel.receive()}")
            }

            joinAll(job1, job2)
            channel.close()

        }
    }

    @Test
    fun testChannelConflated() {
        runBlocking {

            val channel = Channel<Int>(capacity = Channel.CONFLATED)

            val job1 = launch {
                println("send data 1 to channel")
                channel.send(1)
                println("send data 2 to channel")
                channel.send(2)
            }
            job1.join()

            val job2 = launch {
                println("receive data ${channel.receive()}")
            }
            job2.join()

            channel.close()

        }
    }

    @Test
    fun testChannelBufferOverflow() {
        runBlocking {

            val channel = Channel<Int>(capacity = 10, onBufferOverflow = BufferOverflow.DROP_LATEST)

            val job1 = launch {
                repeat(100) {
                    println("send data $it to channel")
                    channel.send(it)
                }
            }
            job1.join()

            val job2 = launch {
                repeat(10) {
                    println("receive data ${channel.receive()}")
                }
            }
            job2.join()

            channel.close()

        }
    }

    @Test
    fun testUndeliveredElement() {
        val channel = Channel<Int>(capacity = 10) { value ->
            println("Undelivered Element $value")
        }
        channel.close()

        runBlocking {
            val job = launch {
                channel.send(10)
                channel.send(100)
            }
            job.join()
        }
    }

    @Test
    fun testProduce() {
        val scope = CoroutineScope(Dispatchers.IO)
//        val channel = Channel<Int>()
//
//        val job1 = scope.launch {
//            repeat(100) {
//                channel.send(it)
//            }
//        }

        val channel: ReceiveChannel<Int> = scope.produce {
            repeat(100) {
                send(it)
            }
        }

        val job2 = scope.launch {
            repeat(100) {
                println(channel.receive())
            }
        }

        runBlocking {
            joinAll(job2)
        }
    }
}