package com.cj.im.callback

/**
 *  Create by chenjiao at  2022/2/7 10:50
 *  描述：简单的数据分发者
 */
interface ISimpleDispatcher<T : Any> : IDispatcher {
    /**
     * 订阅数据,订阅后,订阅者和监听者都建立了关联
     */
    fun observe(owner: Any, observer: ISimpleObserver<T>)

    /**
     * 分发一条数据
     */
    fun dispatch(data: T)
}