package com.cj.sdknet

import android.content.Context
import android.util.Log
import com.cj.sdknet.net.cookie.SimpleCookieJar
import com.cj.sdknet.net.eventlistener.Analysislistener
import com.cj.sdknet.net.interceptor.RequestInterceptor
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：
 */
class OkHttpClientProvider private constructor() {
    private var mClient: OkHttpClient? = null
    private var debugMode = false

    fun init(cacheFile: File, userAgent: String,
            context: Context, debugMode: Boolean,
            analysislistener: Analysislistener, connectTimeOut: Long,
            readTimeOut: Long, writeTimeOut: Long) {
        this.debugMode = debugMode
        //超时参数
        val builder = OkHttpClient.Builder()
            .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
            .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
            .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS)
        builder.cookieJar(SimpleCookieJar())
        var success = false
        if (!cacheFile.exists()) {
            try {
                success = cacheFile.mkdirs()
            } catch (e: Exception) {
                Log.i(TAG, e.message.orEmpty())
            }

        }
        if (success) {
            builder.cache(Cache(cacheFile, MAX_CACHE_SIZE.toLong()))
        }
        builder.connectionPool(ConnectionPool(20, 5, TimeUnit.MINUTES))
        builder.addInterceptor(RequestInterceptor(userAgent, context))
        mClient = builder.build()
    }

    fun getOkhttpClient(): OkHttpClient? {
        checkNotNull(mClient) { "call init() first!" }
        return mClient
    }

    companion object {
        const val MAX_CACHE_SIZE = 10 * 1024 * 1024
        const val TAG = "OkHttpClientProvider"
        var P: OkHttpClientProvider = OkHttpClientProvider()
    }
}