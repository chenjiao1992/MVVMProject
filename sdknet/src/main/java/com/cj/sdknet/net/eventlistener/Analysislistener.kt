package com.cj.sdknet.net.eventlistener

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：
 */
interface Analysislistener {
    fun onConnectStart(url: String, connectIp: String)
    fun analysisNetworkInfo(networkInfo: NetworkInfo)
}