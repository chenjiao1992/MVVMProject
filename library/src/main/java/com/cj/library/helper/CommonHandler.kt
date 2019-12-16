package com.cj.library.helper

import android.os.Handler
import android.os.Looper

object CommonHandler {
    private val mHandler = Handler(Looper.getMainLooper())

    fun post(runnable: Runnable?) {
        if (runnable == null) return
        mHandler.removeCallbacks(runnable)
        mHandler.post(runnable)
    }

    fun post(action: () -> Unit) {
        mHandler.removeCallbacks(action)
        mHandler.post(action)
    }

    fun remove(runnable: Runnable?) {
        if (runnable == null) return
        mHandler.removeCallbacks(runnable)
    }

    fun postDelayed(runnable: Runnable?, delayMillis: Long) {
        if (runnable == null) return
        mHandler.removeCallbacks(runnable)
        mHandler.postDelayed(runnable, delayMillis)
    }

    fun postDelayed(action: () -> Unit, delayMillis: Long) {
        mHandler.removeCallbacks(action)
        mHandler.postDelayed(action, delayMillis)
    }
}