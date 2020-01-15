package com.cj.sdknet.net.eventlistener

import okhttp3.Headers
import okhttp3.Response

class NetworkInfo {
    var url: String? = null //请求的api地址
    var callStartTime: Long = 0 //请求发起的时间
    var callEndTime: Long = 0 //请求结束的时间
    var callFailedTime: Long = 0 //请求失败的时间
    var dnsStartTime: Long = 0 //dns解析开始的时间
    var dnsEndTime: Long = 0 //dns解析结束的时间
    var tcpStartTime: Long = 0 //tcp握手开始的时间
    var tcpEndTime: Long = 0 //tcp握手结束的时间
    var sslStartTime: Long = 0 //ssl握手开始的时间
    var sslEndTime: Long = 0 //ssl握手结束的时间
    var connectFailedTime: Long = 0 //握手失败的时间
    var requestHeadersStartTime: Long = 0 //解析请求头开始的时间
    var requestHeadersEndTime: Long = 0 //解析请求头结束的时间
    var requestBodyStartTime: Long = 0 //解析请求体开始的时间
    var requestBodyEndTime: Long = 0 //解析请求体结束的时间
    var responseHeadersStartTime: Long = 0 //解析响应头开始的时间
    var responseHeadersEndTime: Long = 0 //解析响应头结束的时间
    var responseBodyStartTime: Long = 0 //解析响应体开始的时间
    var responseBodyEndTime: Long = 0 //解析响应体结束的时间
    var requestHeaders: String? = null //请求头的字符串
    var responseHeaders: Headers? = null //响应头的字符串
    var requestBodyLength: Long = 0 //请求体的长度
    var responseBodyLength: Long = 0 //响应体的长度
    var responseCode: Int = 0 //返回的code值
    var error: String? = null //错误信息
    var callTime: Long = 0 //请求耗时
    var dnsTime: Long = 0
        get() = if (dnsStartTime != 0L && dnsEndTime == 0L) {
            -1L
        } else field //dns耗时
    var tcpTime: Long = 0
        get() = if (tcpStartTime != 0L && tcpEndTime == 0L) {
            -1L
        } else field //tcp握手耗时
    var sslTime: Long = 0
        get() = if (sslStartTime != 0L && sslEndTime == 0L) {
            -1L
        } else field //ssl握手耗时
    var requestHeadersTime: Long = 0
        get() = if (requestHeadersStartTime != 0L && requestHeadersEndTime == 0L) {
            -1L
        } else field //解析请求头耗时
    var requestBodyTime: Long = 0
        get() = if (requestBodyStartTime != 0L && requestBodyEndTime == 0L) {
            -1L
        } else field //解析请求体耗时
    var responseHeadersTime: Long = 0
        get() = if (responseHeadersStartTime != 0L && responseHeadersEndTime == 0L) {
            -1L
        } else field //解析响应头耗时
    var responseBodyTime: Long = 0
        get() = if (responseBodyStartTime != 0L && responseBodyEndTime == 0L) {
            -1L
        } else field //解析响应体耗时
    var networkType: Int = 0
    var operator: String? = null
    var connectIp: String? = null
    var ip: String? = null
    var response: Response? = null

}
