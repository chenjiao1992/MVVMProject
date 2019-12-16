package com.cj.library.base

import android.app.Application
import com.cj.library.utils.MetricsUtil

/**
 * Create by chenjiao at 2019/12/6 0006
 * 描述：
 */
class BaseApplicaton : Application() {
    override fun onCreate() {
        super.onCreate()
        MetricsUtil.init(this)
    }
}
