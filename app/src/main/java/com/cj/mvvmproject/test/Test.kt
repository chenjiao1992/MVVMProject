package com.cj.mvvmproject.test

import com.cj.library.extension.orZero
import java.util.concurrent.atomic.AtomicInteger

/**
 *  Create by chenjiao at  2022/2/10 14:55
 *  描述：
 */
object Test {
    private fun atomicTest() {
        val count = AtomicInteger(0)

        for (index in 0 until 100) {
            Thread({
                val incrementAndGet = count.incrementAndGet()
                println("${index}thread--$incrementAndGet")
            }, "${index}thread").start()
        }
        val countThread = ThreadLocal<Int>()
        countThread.set(0)
        for (index in 0 until 100) {
            Thread({
                val value = countThread.get()
                countThread.set(value.orZero() + 1)
                println("${index}countThread--${countThread.get()}")
            }, "${index}countThread").start()
        }
    }



    @JvmStatic
    fun main(args: Array<String>) {
        atomicTest()
    }

}