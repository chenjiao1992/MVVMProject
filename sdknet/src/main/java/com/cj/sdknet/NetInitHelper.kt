package com.cj.sdknet

import android.content.Context
import android.os.Debug
import okhttp3.Interceptor
import java.io.File

/**
 *  Create by chenjiao at 2019/4/11 0011
 *  描述:网络库的初始化操作都在这里
 */
object NetInitHelper {
    var channelId: String? = null
    var provider: RuntimeDataProvider? = null

    fun initProvider(context: Context, debugMode: Boolean,interceptor: Interceptor, param: SdkNetConfigParam) {
        provider = param.dataProvider
        val cacheFile = File(context.cacheDir, "HttpResponseCache")
        OkHttpClientProvider.P.init(
                cacheFile, param.userAgent ?: "", context.applicationContext,
                debugMode, interceptor,param.connectTimeOut, param.readTimeOut, param.writeTimeOut
        )
    }
}