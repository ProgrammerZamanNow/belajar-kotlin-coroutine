package programmer.zaman.now.coroutine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.*

class SuspendFunctionTest {

    suspend fun helloWorld(){
        println("Hello : ${Date()} : ${Thread.currentThread().name}")
        delay(2_000)
        println("World : ${Date()} : ${Thread.currentThread().name}")
    }

    @Test
    fun testSuspendFunction(){
        runBlocking {
            helloWorld()
        }
    }

}