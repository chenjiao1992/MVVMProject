package com.cj.mvvmproject

import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.cj.mvvmproject.databinding.ActivityMainBinding
import com.cj.net.activity.NetDemoActivity
import com.cj.node.activity.BubbleDemoActivity
import com.cj.node.activity.NodeActivity
import com.cj.recyclerview.activity.RecyclerViewActivity
import com.cj.screen.activity.ScreenAdapterActivity
import com.cj.skin.activity.SkinActivity
import com.cj.video.activity.VideoCompressorActivity

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onConfig(arguments: Intent?) {
        binding.onClick = View.OnClickListener {
            when (it.id) {
                R.id.ll_bobbleView ->
                    startActivity(Intent(this, BubbleDemoActivity::class.java))
                R.id.ll_node ->
                    startActivity(Intent(this, NodeActivity::class.java))
                R.id.ll_screen ->
                    startActivity(Intent(this, ScreenAdapterActivity::class.java))
                R.id.ll_video -> {
                    startActivity(Intent(this, VideoCompressorActivity::class.java))
                }
                R.id.ll_skin -> {
                     startActivity(Intent(this, SkinActivity::class.java))
                }
                R.id.ll_net -> {
                    startActivity(Intent(this, NetDemoActivity::class.java))
                }
                R.id.ll_list->{
                    startActivity(Intent(this, RecyclerViewActivity::class.java))
                }
            }
        }
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
