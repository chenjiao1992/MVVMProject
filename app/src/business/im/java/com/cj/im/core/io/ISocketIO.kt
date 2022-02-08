package com.cj.im.core.io

import java.net.Socket

/**
 *  Create by chenjiao at  2022/2/7 11:47
 *  描述：Socket读写接口
 *  所有操作均为同步方式
 */
interface ISocketIO {
    /**
     * 打开指定的socket连接
     * 以便后续进行socket读写操作
     */
    fun openSocket(socket: Socket)

    /**
     * 关闭已经打开的socket连接
     */
    fun closeSocket()

    /**
     * 从打开的socket连接中读取一个byte
     */
    fun readByte():Byte

    /**
     * 向打开的socket连接中读取一个int
     */
    fun writeByte(byte:Byte)
    /**
     * 向打开的socket连接中写入一个int
     */
    fun writeInt(int: Int)

    /**
     * 从打开的socket连接中读取一个short
     */
    fun readShort(): Short

    /**
     * 向打开的socket连接中写入一个short
     */
    fun writeShort(short: Short)

    /**
     * 从打开的socket连接中读取一个long
     */
    fun readLong(): Long

    /**
     * 向打开的socket连接中写入一个long
     */
    fun writeLong(long: Long)

    /**
     * 从打开的socket连接中读取一个byte array
     */
    fun readBytes(count: Int): ByteArray

    /**
     * 向打开的socket连接中写入一个byte array
     */
    fun writeBytes(bytes: ByteArray)

    /**
     * 清空打开的socket的缓冲区，将缓冲区的内容立刻发送至远端
     */
    fun flush()
}