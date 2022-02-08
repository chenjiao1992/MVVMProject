package com.cj.im.callback

/**
 *  Create by chenjiao at  2022/2/7 10:56
 *  描述：数据监听通用基类
 */
interface IObserver<T:Any> {
    /**
     * 通知订阅者,收到了数据
     */
    fun onReceived(data:T)
}