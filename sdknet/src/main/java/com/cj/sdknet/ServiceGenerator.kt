package com.cj.sdknet

import com.cj.sdknet.net.converter.FastJsonConverterFactory
import retrofit2.Retrofit
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.Exception

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：APIService生成器
 */
object ServiceGenerator {
    fun <T> generateService(apiClass: Class<T>): T?{
        return try {
            val okhttpClient = OkHttpClientProvider.P.getOkhttpClient()
            val retrofit = Retrofit.Builder()
                .baseUrl(NetConstants.mock_host)
                .addCallAdapterFactory(CallAdapterFactory.createWithScheduler(Schedulers.io(),
                        AndroidSchedulers.mainThread()))
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(okhttpClient).build()
            retrofit.create(apiClass)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 创建API对象
     *
     * 指定网络请求线程与回调线程
     *
     * @param apiClass
     * @param subscribeOnScheduler
     * @param observeOnScheduler
     * @param <T>
     * @return
    </T> */
    fun <T> generateService(apiClass: Class<T>, subscribeOnScheduler: Scheduler, observeOnScheduler: Scheduler): T {
        val okHttpClient = OkHttpClientProvider.P.getOkhttpClient()
        val retrofit = Retrofit.Builder()
            .baseUrl(NetConstants.mock_host)
            .addCallAdapterFactory(CallAdapterFactory.createWithScheduler(subscribeOnScheduler,
                    observeOnScheduler)) //使用增强的RxJavaCallAdapterFactory,订阅者回调都在主线程中
            .addConverterFactory(FastJsonConverterFactory.create())  //使用自定义转换器
            .client(okHttpClient).build()
        return retrofit.create(apiClass)
    }


    /**
     * 重载方法，用于针对某个Service设置超时时间
     *
     *
     * @param apiClass
     * @param connectTimeOut
     * @param readTimeOut
     * @param writeTimeOut
     * @param <T>
     * @return
    </T> */
    fun <T> generateService(apiClass: Class<T>, connectTimeOut: Long, readTimeOut: Long, writeTimeOut: Long): T {
        val okHttpClient = OkHttpClientProvider.P.getOkhttpClient()!!.newBuilder()
            .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
            .readTimeout(readTimeOut, TimeUnit.MILLISECONDS)
            .writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(NetConstants.mock_host)
            .addCallAdapterFactory(CallAdapterFactory.createWithScheduler(Schedulers.io(),
                    AndroidSchedulers.mainThread())) //使用增强的RxJavaCallAdapterFactory,订阅者回调都在主线程中
            .addConverterFactory(FastJsonConverterFactory.create())  //使用自定义转换器
            .client(okHttpClient).build()
        return retrofit.create(apiClass)
    }
}