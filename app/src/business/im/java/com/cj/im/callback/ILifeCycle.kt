package com.cj.im.callback

import com.cj.im.client.IMClient

/**
 *  Create by chenjiao at  2022/2/7 14:43
 *  描述：生命周期管理通用基类
 */
interface ILifeCycle {
    /**
     *初始化
     */
    fun init(client: IMClient)

    /**
     * 销毁
     */
    fun destroy(client: IMClient)
}