package com.cj.im.model

/**
 *  Create by chenjiao at  2022/2/8 11:19
 *  描述：网络状态
 */
enum class NetworkQuality {
    /**
     * 状态-良好,设备当前网络环境通常为WIF或4G
     */
    Good,

    /**
     * 状态一般,设备当前网络环境通常为2G/3G
     */
    Normal,

    /**
     * 状态-较差,设置当前网络环境通常为GPRS/GPS/EDGE等
     */
    Poor,

    /**
     * 状态不可用,设备当前未接入网络,或网络环境没有信号
     */
    NotAvailable
}