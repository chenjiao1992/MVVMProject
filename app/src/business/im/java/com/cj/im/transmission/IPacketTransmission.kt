package com.cj.im.transmission

import com.cj.im.config.IMConfig
import com.cj.im.model.Packet

/**
 *  Create by chenjiao at  2022/2/7 15:41
 *  描述：数据包传输接口
 *  用于将数据包(Packet)通过socket长连接发送给远端服务器,并通过socket长连接从远端服务器读取数据包
 */
interface IPacketTransmission {
    /**
     * 发送一个数据包(同步方式)
     */
    fun sendSync(config: IMConfig,packet: Packet)

    /**
     * 接收一个数据包（同步方式）
     *
     * 接收过程会阻当前线程，因此必须单独分配一个线程专门用于接收数据包
     *
     * @param config IM SDK配置参数
     * @return 数据包
     */
    fun receiveSync(config: IMConfig): Packet
}