package com.cj.mvvmproject

import android.content.pm.ApplicationInfo
import com.cj.library.base.BaseApplicaton
import com.cj.sdknet.NetInitHelper
import com.cj.sdknet.SdkNetConfigParam
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：
 */
class MMApplication : BaseApplicaton() {
    override fun onCreate() {
        super.onCreate()
        //网络初始化
        DEBUG = applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        NetInitHelper.initProvider(this, DEBUG, StethoInterceptor(), SdkNetConfigParam())
        if (DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    companion object {
        var DEBUG = false
    }
}