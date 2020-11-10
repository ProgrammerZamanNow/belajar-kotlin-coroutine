package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class CoroutineScopeTest {

    @Test
    fun testScope() {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            delay(2000)
            println("Run ${Thread.currentThread().name}")
        }

        scope.launch {
            delay(2000)
            println("Run ${Thread.currentThread().name}")
        }

        runBlocking {
            delay(2000)
            println("Done")
        }
    }

    @Test
    fun testScopeCancel() {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            delay(2000)
            println("Run ${Thread.currentThread().name}")
        }

        scope.launch {
            delay(2000)
            println("Run ${Thread.currentThread().name}")

            coroutineScope {

            }
        }

        runBlocking {
            delay(1000)
            scope.cancel()
            delay(2000)
            println("Done")
        }
    }

    suspend fun getFoo(): Int {
        delay(1000)
        println("Foo ${Thread.currentThread().name}")
        return 10
    }

    suspend fun getBar(): Int {
        delay(1000)
        println("Bar ${Thread.currentThread().name}")
        return 10
    }

    suspend fun getSum(): Int = coroutineScope {
        val foo = async { getFoo() }
        val bar = async { getBar() }
        foo.await() + bar.await()
    }

    suspend fun getSumManual(): Int {
        val scope = CoroutineScope(Dispatchers.IO)
        val foo = scope.async { getFoo() }
        val bar = scope.async { getBar() }
        return foo.await() + bar.await()
    }

    @Test
    fun testCoroutineScopeFunction() {
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            val result = getSum()
            println("Result $result")
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testParentChildDispatcher() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        val job = scope.launch {
            println("Parent Scope : ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    println("Child Scope : ${Thread.currentThread().name}")
                }
            }
        }

        runBlocking {
            job.join()
        }
    }

    @Test
    fun testParentChildCancel() {
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        val job = scope.launch {
            println("Parent Scope : ${Thread.currentThread().name}")
            coroutineScope {
                launch {
                    delay(2000)
                    println("Child Scope : ${Thread.currentThread().name}")
                }
            }
        }

        runBlocking {
            job.cancelAndJoin()
        }
    }
}