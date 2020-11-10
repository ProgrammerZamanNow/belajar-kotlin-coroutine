package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class SupervisorJobTest {

    @Test
    fun testJob() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + Job())

        val job1 = scope.launch {
            delay(2000)
            println("Job 1 Done")
        }

        val job2 = scope.launch {
            delay(1000)
            throw IllegalArgumentException("Job 2 failed")
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun testSupervisorJob() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + SupervisorJob())

        val job1 = scope.launch {
            delay(2000)
            println("Job 1 Done")
        }

        val job2 = scope.launch {
            delay(1000)
            throw IllegalArgumentException("Job 2 failed")
        }

        runBlocking {
            joinAll(job1, job2)
        }
    }

    @Test
    fun testSupervisorScopeFunction() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher + Job())

        runBlocking {
            scope.launch {
                supervisorScope {
                    launch {
                        delay(2000)
                        println("Child 1 Done")
                    }
                    launch {
                        delay(1000)
                        throw IllegalArgumentException("Child 2 Error")
                    }
                }
            }

            delay(3000)
        }
    }

    @Test
    fun testJobExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            println("Error ${throwable.message}")
        }

        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        runBlocking {
            val job = scope.launch {
                launch(exceptionHandler) { // not used
                    println("Job Child")
                    throw IllegalArgumentException("Child Error")
                }
            }

            job.join()
        }
    }

    @Test
    fun testSupervisorJobExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            println("Error ${throwable.message}")
        }

        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        runBlocking {
            val job = scope.launch {
                supervisorScope {
                    launch(exceptionHandler) {
                        launch(exceptionHandler) { // not used
                            println("Job Child")
                            throw IllegalArgumentException("Child Error")
                        }
                    }
                }
            }

            job.join()
        }
    }
}