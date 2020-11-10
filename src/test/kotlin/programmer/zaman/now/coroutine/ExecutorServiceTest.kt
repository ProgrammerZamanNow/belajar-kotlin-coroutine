package programmer.zaman.now.coroutine

import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.Executors

class ExecutorServiceTest {

    @Test
    fun testSingleThreadPool(){
        val executorService = Executors.newSingleThreadExecutor()
        repeat(10){
            val runnable = Runnable {
                Thread.sleep(1000)
                println("Done $it ${Thread.currentThread().name} ${Date()}")
            }
            executorService.execute(runnable)
            println("Selesai memasukkan runnable $it")
        }

        println("MENUNGGU")
        Thread.sleep(11_000)
        println("SELESAI")
    }

    @Test
    fun testFixThreadPool(){
        val executorService = Executors.newFixedThreadPool(3)
        repeat(10){
            val runnable = Runnable {
                Thread.sleep(1000)
                println("Done $it ${Thread.currentThread().name} ${Date()}")
            }
            executorService.execute(runnable)
            println("Selesai memasukkan runnable $it")
        }

        println("MENUNGGU")
        Thread.sleep(11_000)
        println("SELESAI")
    }

    @Test
    fun testCacheThreadPool(){
        val executorService = Executors.newCachedThreadPool()
        repeat(100){
            val runnable = Runnable {
                Thread.sleep(1000)
                println("Done $it ${Thread.currentThread().name} ${Date()}")
            }
            executorService.execute(runnable)
            println("Selesai memasukkan runnable $it")
        }

        println("MENUNGGU")
        Thread.sleep(11_000)
        println("SELESAI")
    }

}