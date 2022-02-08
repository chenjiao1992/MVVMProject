package com.cj.im.heartbeat

import com.cj.im.callback.ILifeCycle
import com.cj.im.config.IMConfig

/**
 *  Create by chenjiao at  2022/2/7 14:42
 *  描述：心跳策略接口
 */
interface IHeartbeat : ILifeCycle {
    /**
     * 向IM服务器发送一个心跳包
     */
    fun send(config: IMConfig, interval: Long)

    /**
     * 根据IM服务器返回的上一个心跳包回复数据,调整下一个心跳包发送延时
     * @param config IM SDK配置参数
     * @param success 上一个心跳包是否发送成功
     * @return 下一个心跳包发送延时（单位-毫秒）
     */
    fun adjustInterval(config: IMConfig, success: Boolean): Long

    /**
     * 重置下一个心跳包发送延时为默认值
     *
     * @param config IM SDK配置参数
     */
    fun resetInterval(config: IMConfig)
}