package com.cj.node.activity

import android.content.Intent
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ActivityBubbleBinding

/**
 *  Create by chenjiao at 2019/11/28 0028
 *  描述：
 */
class BubbleDemoActivity : BaseBindingActivity<ActivityBubbleBinding>() {

    override fun getLayoutId()=R.layout.activity_bubble

    override fun onConfig(arguments: Intent?) {
    }



}