package com.cj.net.activity

import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ActivityNetDemoBinding
import com.cj.net.api.MainApi
import com.cj.net.api.Province
import com.cj.sdknet.ServiceGenerator
import com.cj.sdknet.net.callback.APISubscriber

/**
 *  Create by chenjiao at 2021/2/19 0019
 *  描述：
 */


class NetDemoActivity : BaseBindingActivity<ActivityNetDemoBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_net_demo

    override fun onConfig(arguments: Intent?) {
        ServiceGenerator.generateService(MainApi::class.java)?.getProvices("http://guolin.tech")
            ?.subscribe(object : APISubscriber<ArrayList<Province>>() {
                override fun onNext(t: ArrayList<Province>?) {
                    Toast.makeText(this@NetDemoActivity, "onNext=${t?.size}", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "onNext=${t?.size}")
                }

                override fun onError(code: Int, message: String?, t: Throwable?) {
                    Toast.makeText(this@NetDemoActivity, message, Toast.LENGTH_SHORT).show()
                    //todo 这个接口调用会提示异常,是jsonArray不能转换为jsonObject,因为是因为 FastJsonResponseBodyConverter中需要与服务端约定好传data,和msg
                }

            })
    }

}
