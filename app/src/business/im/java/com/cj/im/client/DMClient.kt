package com.cj.im.client

/**
 *  Create by chenjiao at  2022/2/7 09:58
 *  描述：
 */
object DMClient {
    val initialized
        get() = instance != null
    var instance: IMClient? = null
        private set

    fun initialize() {
        var client = instance
        client?.refreshConnection()
    }

}