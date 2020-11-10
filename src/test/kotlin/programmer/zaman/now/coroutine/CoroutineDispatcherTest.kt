package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class CoroutineDispatcherTest {

    @Test
    fun testDispatcher() {
        runBlocking {
            println("runBlocking ${Thread.currentThread().name}")
            val job1 = GlobalScope.launch(Dispatchers.Default) {
                println("Job 1 ${Thread.currentThread().name}")
            }
            val job2 = GlobalScope.launch(Dispatchers.IO) {
                println("Job 2 ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }

    @Test
    fun testUnconfined() {
        runBlocking {
            println("runBlocking ${Thread.currentThread().name}")

            GlobalScope.launch(Dispatchers.Unconfined) {
                println("Unconfined : ${Thread.currentThread().name}")
                delay(1000)
                println("Unconfined : ${Thread.currentThread().name}")
                delay(1000)
                println("Unconfined : ${Thread.currentThread().name}")
            }
            GlobalScope.launch {
                println("Confined : ${Thread.currentThread().name}")
                delay(1000)
                println("Confined : ${Thread.currentThread().name}")
                delay(1000)
                println("Confined : ${Thread.currentThread().name}")
            }

            delay(3000)
        }
    }

    @Test
    fun testExecutorService() {
        val dispatcherService = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val dispatcherWeb = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        runBlocking {
            val job1 = GlobalScope.launch(dispatcherService) {
                println("Job 1 : ${Thread.currentThread().name}")
            }
            val job2 = GlobalScope.launch(dispatcherWeb) {
                println("Job 2 : ${Thread.currentThread().name}")
            }
            joinAll(job1, job2)
        }
    }

    @Test
    fun testWithContext() {
        val dispatcherClient = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        runBlocking {
            val job = GlobalScope.launch(Dispatchers.IO) {
                println("1 ${Thread.currentThread().name}")
                withContext(dispatcherClient) {
                    println("2 ${Thread.currentThread().name}")
                }
                println("3 ${Thread.currentThread().name}")
                withContext(dispatcherClient) {
                    println("4 ${Thread.currentThread().name}")
                }
            }
            job.join()
        }
    }

    @Test
    fun testCancelFinally() {
        runBlocking {
            val job = GlobalScope.launch {
                try {
                    println("Start job")
                    delay(1000)
                    println("End job")
                } finally {
                    println(isActive)
                    delay(1000)
                    println("Finally")
                }
            }
            job.cancelAndJoin()
        }
    }

    @Test
    fun testNonCancellable() {
        runBlocking {
            val job = GlobalScope.launch {
                try {
                    println("Start job")
                    delay(1000)
                    println("End job")
                } finally {
                    withContext(NonCancellable) {
                        println(isActive)
                        delay(1000)
                        println("Finally")
                    }
                }
            }
            job.cancelAndJoin()
        }
    }
}