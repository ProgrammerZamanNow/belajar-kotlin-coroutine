package programmer.zaman.now.coroutine

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.concurrent.thread

class ThreadTest {

    @Test
    fun testThreadName() {
        val threadName = Thread.currentThread().name
        println("Running in thread $threadName")
    }

    @Test
    fun testNewThread() {
//        val runnable = Runnable {
//            println(Date())
//            Thread.sleep(2000)
//            println("Finish : ${Date()}")
//        }
//
//        val thread = Thread(runnable)
//        thread.start()

        thread(start = true) {
            println(Date())
            Thread.sleep(2000)
            println("Finish : ${Date()}")
        }

        println("MENUNGGU SELESAI")
        Thread.sleep(3000)
        println("SELESAI")
    }

    @Test
    fun testMultipleThread() {
        val thread1 = Thread(Runnable {
            println(Date())
            Thread.sleep(2000)
            println("Finish Thread 1 : ${Thread.currentThread().name} : ${Date()}")
        })
        val thread2 = Thread(Runnable {
            println(Date())
            Thread.sleep(2000)
            println("Finish Thread 2 : ${Thread.currentThread().name} : ${Date()}")
        })

        thread1.start()
        thread2.start()

        println("MENUNGGU SELESAI")
        Thread.sleep(3000)
        println("SELESAI")
    }
}