package com.cj.mvvmproject

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cj.library.base.BaseBindingActivity
import com.cj.mvvmproject.databinding.ActivityMainBinding
import com.cj.mvvmproject.node.NodeActivity

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
