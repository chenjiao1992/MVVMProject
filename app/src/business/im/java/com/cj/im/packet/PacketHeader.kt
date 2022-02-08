package com.cj.im.packet

import com.cj.im.model.Constants
import java.nio.ByteBuffer

/**
 *  Create by chenjiao at  2022/2/7 16:05
 *  描述：内建的数据包头
 */
class PacketHeader internal constructor() {
    var version: Short = 0
        private set
    var operation = 0
        private set
    var sequenceId = 0
        internal set

    fun apply(bytes: ByteArray) {
        val byteBuffer = ByteBuffer.wrap(bytes)
        version = byteBuffer.short
        operation = byteBuffer.int
        sequenceId = byteBuffer.int
    }

    fun extract(): ByteArray {
        val byteBuffer = ByteBuffer.allocate(10)
        byteBuffer.putShort(version)
            .putInt(operation)
            .putInt(sequenceId)
        return byteBuffer.array()
    }

    companion object {
        const val HEADER_LENGTH = 16

        fun create(operation: Int): PacketHeader {
            val header = PacketHeader()
            header.version = Constants.VERSION_CODES
            header.operation = operation
            return header
        }
    }
}