package programmer.zaman.now.coroutine

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

class LockingTest {

    @Test
    fun testRaceCondition() {
        var counter: Int = 0
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        repeat(100) {
            scope.launch {
                repeat(1000) {
                    counter++
                }
            }
        }

        runBlocking {
            delay(5000)
            println("Total Counter : $counter")
        }
    }

    @Test
    fun testMutex() {
        var counter: Int = 0
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val mutex = Mutex()

        repeat(100) {
            scope.launch {
                repeat(1000) {
                    mutex.withLock {
                        counter++
                    }
                }
            }
        }

        runBlocking {
            delay(5000)
            println("Total Counter : $counter")
        }
    }

    @Test
    fun testSemaphore() {
        var counter: Int = 0
        val dispatcher = Executors.newFixedThreadPool(10).asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val semaphore = Semaphore(permits = 2)

        repeat(100) {
            scope.launch {
                repeat(1000) {
                    semaphore.withPermit {
                        counter++
                    }
                }
            }
        }

        runBlocking {
            delay(5000)
            println("Total Counter : $counter")
        }
    }
}