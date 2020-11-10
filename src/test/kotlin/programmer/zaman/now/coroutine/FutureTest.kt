package programmer.zaman.now.coroutine

import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.system.measureTimeMillis

class FutureTest {

    val executorService = Executors.newFixedThreadPool(10)

    fun getFoo(): Int {
        Thread.sleep(5000)
        return 10
    }

    fun getFoo1(): Int {
        Thread.sleep(5000)
        return 10
    }

    fun getBar(): Int {
        Thread.sleep(5000)
        return 10
    }

    fun getBar1(): Int {
        Thread.sleep(5000)
        return 10
    }

    @Test
    fun testNonParallel(){
        val time = measureTimeMillis {
            val foo = getFoo()
            val bar = getBar()
            val foo1 = getFoo1()
            val bar1 = getBar1()
            val result = foo + bar + foo1 + bar1
            println("Total : $result")
        }
        println("Total time : $time")
    }

    @Test
     fun testFuture() {
        val time = measureTimeMillis {
            val foo: Future<Int> = executorService.submit(Callable { getFoo() })
            val bar: Future<Int> = executorService.submit(Callable { getBar() })
            val foo1: Future<Int> = executorService.submit(Callable { getFoo1() })
            val bar1: Future<Int> = executorService.submit(Callable { getBar1() })

            val result = foo.get() + bar.get() + foo1.get() + bar1.get()
            println("Total : $result")
        }
        println("Total time $time")
    }
}