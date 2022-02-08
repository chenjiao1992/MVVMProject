package com.cj.im.callback

/**
 *  Create by chenjiao at  2022/2/7 10:48
 *  描述：数据分发通用基类
 */
interface IDispatcher {
    fun ignore(owner: Any)

    fun destroy()
}