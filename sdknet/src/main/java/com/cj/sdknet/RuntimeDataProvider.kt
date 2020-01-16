package com.cj.sdknet

/**
 *  Create by chenjiao at 2019/4/11 0011
 *  描述：
 */

interface RuntimeDataProvider {
    /**
     * 获取UUID
     */
    fun getDid(): String

    /**
     *获取设备id
     */
    fun getDeviceId(): String

    /**
     * 获取用户id
     */
    fun getUid(): String
}