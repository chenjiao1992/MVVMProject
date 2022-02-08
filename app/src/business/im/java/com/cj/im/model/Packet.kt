package com.cj.im.model

import com.cj.im.packet.IPacketBody
import com.cj.im.packet.PacketHeader

/**
 *  Create by chenjiao at  2022/2/7 15:44
 *  描述：数据包
 *  数据包由包头（PacketHeader）和包体（PacketBody）两部分组成，包头用来储存数据包体的长度、内容类型等信息，包体则是具体的数据内容
 *
 * @param header 数据包头
 * @param body 数据包体
 */
class Packet internal constructor(val header: PacketHeader, val body: IPacketBody) {
    companion object {
        fun create(operation: Int, body: IPacketBody): Packet {
            val header = PacketHeader.create(operation)
            return Packet(header, body)
        }
    }
}