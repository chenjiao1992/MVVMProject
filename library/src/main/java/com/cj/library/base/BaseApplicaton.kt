package com.cj.library.base

import android.app.Application
import com.cj.library.utils.MetricsUtil
import skin.support.SkinCompatManager
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.design.app.SkinMaterialViewInflater
import java.lang.ref.WeakReference

/**
 * Create by chenjiao at 2019/12/6 0006
 * 描述：
 */
open class BaseApplicaton : Application() {
    override fun onCreate() {
        super.onCreate()
        MetricsUtil.init(this)
        //换肤初始化 https://github.com/ximsfei/Android-skin-support
        SkinCompatManager.withoutActivity(this)
            .addInflater(SkinAppCompatViewInflater()) // 基础控件换肤初始化
            .addInflater(SkinMaterialViewInflater()) // material design 控件换肤初始化[可选]
            .addInflater(SkinConstraintViewInflater()) // ConstraintLayout 控件换肤初始化[可选]
            .addInflater(SkinCardViewInflater()) // CardView v7 控件换肤初始化[可选]
            .setSkinStatusBarColorEnable(false) // 关闭状态栏换肤，默认打开[可选]
            .setSkinWindowBackgroundEnable(false) // 关闭windowBackground换肤，默认打开[可选]
            .loadSkin()
    }

    companion object {
        private var mInstanceRef: WeakReference<BaseApplicaton>? = null
        val instance
            get() = mInstanceRef?.get()
    }
}
