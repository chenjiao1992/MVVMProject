package com.cj.screen.activity

import android.content.Intent
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ActivityScreenAdapterBinding

/**
 * Create by chenjiao at 2019/12/6 0006
 * 描述：屏幕适配方案demo
 */
class ScreenAdapterActivity : BaseBindingActivity<ActivityScreenAdapterBinding>(R.layout.activity_screen_adapter) {
    override fun onConfig(arguments: Intent) {}
}