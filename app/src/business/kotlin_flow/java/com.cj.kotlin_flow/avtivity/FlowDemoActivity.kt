package com.cj.kotlin_flow.avtivity

import android.content.Intent
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.cj.kotlin_flow.FlowViewModel
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ActivityFlowDemoBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * flow相关api学习
 */
class FlowDemoActivity : BaseBindingActivity<ActivityFlowDemoBinding>() {

    private val flowViewModel by viewModels<FlowViewModel>()//新写法
    override fun getLayoutId() = R.layout.activity_flow_demo

    override fun onConfig(arguments: Intent?) {
        binding.tvStart.setOnClickListener {
            lifecycleScope.launch {
                flowViewModel.timeFlow.collectLatest {
                    binding.tvTime.text = it.toString()
                    Log.d("flow", "time=$it")
                    delay(3000L)
                }
            }

        }
    }


    companion object {
        //kotlin中定义main函数必须在 companion object中定义,并且需要注解
        //执行main函数时提示 Command line is too long等一长段提示,解决办法:解決辦法:
        //1. 左上角檔案瀏覽選項切換到 “Project”
        //2. 打開 .idea 目錄底下的 workspace.xml
        //3. 找到 <component name=”PropertiesComponent”>
        //4. 裡面加入以下屬性
        //<property name=”dynamic.classpath” value=”true” />
        //5. 執行測試就 OK了
        @JvmStatic
        fun main(args: Array<String>) {
            runBlocking {
                val flow = flowOf(1, 2, 3, 4, 5, 6)
                //map,filter等操作符要么连着flowOf写,要么之后连着collect,不然不生效
                flow.filter {//过滤出满足条件的数据
                    it % 2 == 0
                }.onEach { //遍历每个数据
                    println(it)
                }.map { //map是对发射的数据做运算
                    it * it
                }.collect {
                    println(it)
                }
            }
        }
    }
}