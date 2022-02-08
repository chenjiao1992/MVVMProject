package com.cj.im.client

import android.content.Context
import com.cj.im.config.IMConfig
import com.cj.im.model.NetworkQuality
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  Create by chenjiao at  2022/2/7 10:01
 *  描述：
 */
class IMClient(val applicationContext: Context, val config: IMConfig) {
    //client连接状态
    var status: Int
        get() = config.status
        internal set(value) {
            config.status = value
        }

    //网络状态
    var quality = NetworkQuality.NotAvailable
        private set
    val idled: Boolean
        get() = status == STATUS_IDLE

    //断开连接标志位
    private val mDisconnectFlag = AtomicBoolean(false)

    //登出标志
    private val mLogoutFlag = AtomicBoolean(false)

    //重试连接次数
    private var mClientRetryCount = 0

    fun refreshConnection() {
        val status = status
        // 为什么状态是已鉴权直接return
        if (status == STATUS_LOGINING || status == STATUS_LOGINED || quality == NetworkQuality.NotAvailable) return
        if (status != STATUS_IDLE && status != STATUS_RECONNECTING && status != STATUS_CONNECTING_RECONNECT_SERVER) return
        this.status = STATUS_IDLE

    }

    fun connect() {
        if (!idled) return
        mDisconnectFlag.set(false)
        status = STATUS_CONNECTING_RESET
        mClientRetryCount=0

    }

    companion object {
        //状态-断开
        const val STATUS_IDLE = 0

        //状态-连接中
        const val STATUS_CONNECTING = 1 shl 8

        //状态-连接中-重置中
        const val STATUS_CONNECTING_RESET = 1 or STATUS_CONNECTING

        //状态-连接中-获取网关gateway
        const val STATUS_CONNECTING_FETCH_GATEWAY = 2 or STATUS_CONNECTING

        //状态-连接中-ping server
        const val STATUS_CONNECTING_PING_SERVERS = 3 or STATUS_CONNECTING

        //状态-连接中-连接server
        const val STATUS_CONNECTING_CONNECT_SERVER = 4 or STATUS_CONNECTING

        //状态-连接中-重连server
        const val STATUS_CONNECTING_RECONNECT_SERVER = 5 or STATUS_CONNECTING

        //状态-连接中-打开socket
        const val STATUS_CONNECTING_OPEN_SOCKET = 6 or STATUS_CONNECTING

        //状态-鉴权中
        const val STATUS_LOGINING = 3 shl 8

        //状态-已鉴权
        const val STATUS_LOGINED = 4 shl 8

        //状态-重连中
        const val STATUS_RECONNECTING = 5 shl 8

        //传输
        //传输-发送心跳中
        const val TRANSMISSION_HEARTBEATING = 1 shl 8

        //传输-发送数据中
        const val TRANSMISSION_SENDING = 2 shl 8

        //传输-发送数据中-包数据
        const val TRANSMISSION_SENDING_PACKET = 1 or TRANSMISSION_SENDING

        //传输-发送数据中-流数据
        const val TRANSMISSION_SENDING_STREAM = 2 or TRANSMISSION_SENDING

        //传输-发送数据中-http数据
        const val TRANSMISSION_SENDING_HTTP = 3 or TRANSMISSION_SENDING

        //传输-接受数据中
        const val TRANSMISSION_RECEIVING = 3 shl 8

        //传输-接受数据中-包数据
        const val TRANSMISSION_RECEIVING_PACKET = 1 or TRANSMISSION_RECEIVING

        //传输-接受数据中-流数据
        const val TRANSMISSION_RECEIVING_STEAM = 2 or TRANSMISSION_RECEIVING

        //传输-接受数据中-http数据
        const val TRANSMISSION_RECEIVING_HTTP = 3 or TRANSMISSION_RECEIVING
    }
}