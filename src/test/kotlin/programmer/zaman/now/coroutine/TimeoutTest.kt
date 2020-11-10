package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.*

class TimeoutTest {

    @Test
    fun testTimeout(){
        runBlocking {
            val job = GlobalScope.launch {
                println("Start Coroutine")
                withTimeout(5000){
                    repeat(100){
                        delay(1000)
                        println("$it ${Date()}")
                    }
                }
                println("Finish Coroutine")
            }
            job.join()
        }
    }

    @Test
    fun testTimeoutOrNull(){
        runBlocking {
            val job = GlobalScope.launch {
                println("Start Coroutine")
                withTimeoutOrNull(5000){
                    repeat(100){
                        delay(1000)
                        println("$it ${Date()}")
                    }
                }
                println("Finish Coroutine")
            }
            job.join()
        }
    }

}