package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.concurrent.thread

class CoroutineTest {

    suspend fun hello() {
        delay(1_000)
        println("Hello World")
    }

    @Test
    fun testCoroutine() {
        GlobalScope.launch {
            hello()
        }

        println("MENUNGGU")
        runBlocking {
            delay(2_000)
        }
        println("SELESAI")
    }

    @Test
    fun testThread() {
        repeat(10000) {
            thread {
                Thread.sleep(1_000)
                println("Done $it : ${Date()}")
            }
        }

        println("Waiting")
        Thread.sleep(3_000)
        println("Finish")
    }

    @Test
    fun testCoroutineMany() {
        repeat(100_000) {
            GlobalScope.launch {
                delay(1_000)
                println("Done $it : ${Date()} ${Thread.currentThread().name}")
            }
        }

        println("Waiting")
        runBlocking {
            delay(3_000)
        }
        println("Finish")
    }

    @Test
    fun testParentChild() {
        runBlocking {
            val job = GlobalScope.launch {
                launch {
                    delay(2000)
                    println("Child 1 Done")
                }
                launch {
                    delay(4000)
                    println("Child 2 Done")
                }
                delay(1000)
                println("Parent Done")
            }

            job.join()
        }
    }

    @Test
    fun testParentChildCancel() {
        runBlocking {
            val job = GlobalScope.launch {
                launch {
                    delay(2000)
                    println("Child 1 Done")
                }
                launch {
                    delay(4000)
                    println("Child 2 Done")
                }
                delay(1000)
                println("Parent Done")
            }

            job.cancelChildren()
            job.join()
        }
    }

    @Test
    fun testAwaitCancellation() {
        runBlocking {
            val job = launch {
                try {
                    println("Job start")
                    awaitCancellation()
                } finally {
                    println("Cancelled")
                }
            }

            delay(5000)
            job.cancelAndJoin()
        }
    }
}