package com.cj.library.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *  Create by chenjiao at 2019/12/13 0013
 *  描述：
 */
abstract class BaseBindingActivity<BINDING : ViewDataBinding> : BaseHandlePermissionActivity() {
    protected lateinit var binding: BINDING
    override fun doInflate(activity: BaseActivity, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(activity, getLayoutId())
    }

    abstract fun getLayoutId(): Int

    override fun onDestroy() {
        if (::binding.isInitialized) {
            binding.unbind()
        }
        super.onDestroy()
    }

}