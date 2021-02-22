package com.cj.recyclerview.activity

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.mvvmproject.R
import com.cj.mvvmproject.SmartRefreshUtils
import com.cj.mvvmproject.databinding.ActivityRecyclerviewBinding
import com.cj.recyclerview.FunctionBean
import com.cj.recyclerview.adapter.DemoAdapter

/**
 *  Create by chenjiao at 2021/2/22 0022
 *  描述：
 */
class RecyclerViewActivity : BaseBindingActivity<ActivityRecyclerviewBinding>() {
    override fun getLayoutId() = R.layout.activity_recyclerview

    override fun onConfig(arguments: Intent?) {
        binding.rv.layoutManager = LinearLayoutManager(this)
        val mSmartRefreshUtils = SmartRefreshUtils.with(binding.srl)
        mSmartRefreshUtils.pureScrollMode()
        val mList = initData()
        val adapter = DemoAdapter(mList)
        binding.rv.adapter = adapter
    }

    private fun initData(): ArrayList<FunctionBean> {
        val list = ArrayList<FunctionBean>()
        list.add(FunctionBean("气泡页面", R.drawable.icon_bubble))
        list.add(FunctionBean("对话框", R.drawable.icon_dialog))
        list.add(FunctionBean("视频压缩", R.drawable.icon_video))
        list.add(FunctionBean("屏幕适配", R.drawable.icon_screen))
        list.add(FunctionBean("换肤", R.drawable.icon_skin))
        list.add(FunctionBean("网络请求", R.drawable.icon_net))
        return list
    }

}