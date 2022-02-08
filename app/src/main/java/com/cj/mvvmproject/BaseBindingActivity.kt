package com.cj.mvvmproject

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.cj.library.base.BaseActivity
import com.cj.library.base.BaseHandlePermissionActivity
import com.cj.library.extension.setStatusBarLightMode
import skin.support.observe.SkinObservable

/**
 *  Create by chenjiao at 2019/12/13 0013
 *  描述：
 */
abstract class BaseBindingActivity<BINDING : ViewDataBinding>(@LayoutRes var layoutId:Int) : BaseHandlePermissionActivity() {
    protected lateinit var binding: BINDING

    override fun doInflate(activity: BaseActivity, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(activity, layoutId)
    }


    override fun onDestroy() {
        if (::binding.isInitialized) {
            binding.unbind()
        }
        super.onDestroy()
    }

    override fun updateSkin(observable: SkinObservable?, o: Any?) {
        setStatusBarLightMode(!isDarkTheme)
    }

     val isDarkTheme: Boolean
        get() {
            val themeName = AccountManager.themeName
            return isDarkTheme(themeName)
        }

    fun isDarkTheme(themeName: String) = themeName == "red"
}