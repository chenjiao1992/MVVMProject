package com.cj.mvvmproject

import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cj.library.base.BaseBindingActivity
import com.cj.mvvmproject.api.MainApi
import com.cj.mvvmproject.api.Province
import com.cj.mvvmproject.databinding.ActivityMainBinding
import com.cj.mvvmproject.node.NodeActivity
import com.cj.sdknet.ServiceGenerator
import com.cj.sdknet.net.callback.APISubscriber

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onConfig(arguments: Intent?) {
        binding.onClick = View.OnClickListener {
            when (it.id) {
                R.id.tvBobbleView ->
                    startActivity(Intent(this, BubbleDemoActivity::class.java))
                R.id.tvNode ->
                    startActivity(Intent(this, NodeActivity::class.java))
                R.id.tvScreenAdapter ->
                    startActivity(Intent(this, ScreenAdapterActivity::class.java))
            }
        }
        ServiceGenerator.generateService(MainApi::class.java)?.getProvices("http://guolin.tech")
            ?.subscribe(object : APISubscriber<ArrayList<Province>>() {
                override fun onNext(t: ArrayList<Province>?) {
                    Toast.makeText(this@MainActivity, "onNext=${t?.size}", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "onNext=${t?.size}")
                }

                override fun onError(code: Int, message: String?, t: Throwable?) {
                    Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                    //todo 这个接口调用会提示异常,是jsonArray不能转换为jsonObject,因为是因为 FastJsonResponseBodyConverter中需要与服务端约定好传data,和msg
                }

            })
    }

    override fun onPrepared() {

    }

    override fun onToolbarMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menuContact -> {
                Toast.makeText(this@MainActivity, "消息", Toast.LENGTH_SHORT).show()
            }
            R.id.menuCreateGroup -> {
                Toast.makeText(this@MainActivity, "群", Toast.LENGTH_SHORT).show()
            }
            R.id.menuAdd -> {
                Toast.makeText(this@MainActivity, "添加", Toast.LENGTH_SHORT).show()
            }
            R.id.menuSecretary -> {
                Toast.makeText(this@MainActivity, "秘书", Toast.LENGTH_SHORT).show()
            }
            R.id.menuQRCodeScan -> {
                Toast.makeText(this@MainActivity, "扫一扫", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onToolbarMenuItemClick(menuItem)
    }

}
