package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class AsyncTest {

    suspend fun getFoo():Int {
        delay(1000)
        return 10
    }

    suspend fun getBar():Int {
        delay(1000)
        return 10
    }

    @Test
    fun testAsync(){
        runBlocking {
            val time = measureTimeMillis {
                val foo: Deferred<Int> = GlobalScope.async { getFoo() }
                val bar: Deferred<Int> = GlobalScope.async { getBar() }

                val result = foo.await() + bar.await()
                println("Result : $result")
            }
            println("Total time : $time")
        }
    }

    @Test
    fun testAwaitAll(){
        runBlocking {
            val time = measureTimeMillis {
                val foo1: Deferred<Int> = GlobalScope.async { getFoo() }
                val bar1: Deferred<Int> = GlobalScope.async { getBar() }
                val foo2: Deferred<Int> = GlobalScope.async { getFoo() }
                val bar2: Deferred<Int> = GlobalScope.async { getBar() }

                val result = awaitAll(foo1, foo2, bar1, bar2).sum()
                println("Result : $result")
            }
            println("Total time : $time")
        }
    }

}