package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class ExceptionHandlingTest {

    @Test
    fun testExceptionLaunch() {
        runBlocking {
            val job = GlobalScope.launch {
                println("Start coroutine")
                throw IllegalArgumentException()
            }

            job.join()
            println("Finish")
        }
    }

    @Test
    fun testExceptionAsync() {
        runBlocking {
            val deferred = GlobalScope.async {
                println("Start coroutine")
                throw IllegalArgumentException()
            }

            try {
                deferred.await()
            } catch (error: IllegalArgumentException) {
                println("Error")
            } finally {
                println("Finish")
            }
        }
    }

    @Test
    fun testExceptionHandler() {
        val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            println("Ups error ${throwable.message}")
        }

        val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

        runBlocking {
            val job = GlobalScope.launch(exceptionHandler) {
                println("Start coroutine")
                throw IllegalArgumentException("error")
            }

            job.join()
            println("Finish")

            val job2 = scope.launch {
                println("Start coroutine")
                throw IllegalArgumentException("error")
            }
            job2.join()
            println("Finish")
        }
    }
}