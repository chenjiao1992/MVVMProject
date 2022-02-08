package com.cj.im.packet

import com.cj.im.config.IMConfig

/**
 *  Create by chenjiao at  2022/2/7 18:15
 *  描述：数据包体
 */
interface IPacketBody {
    /**
     * 根据给定ByteArray解析数据包体
     */
    fun apply(config: IMConfig, bytes: ByteArray)

    /**
     * 将数据包体转化为byteArray
     */
    fun extract(config: IMConfig): ByteArray
}