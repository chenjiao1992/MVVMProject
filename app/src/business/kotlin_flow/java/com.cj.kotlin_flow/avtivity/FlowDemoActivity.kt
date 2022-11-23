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
            //简单操作符
            runBlocking {
                val flow = flowOf(1, 2, 3, 4, 5, 6)
                //map,filter等操作符要么连着flowOf写,要么之后连着collect,不然不生效
                flow.filter {//过滤出满足条件的数据
                    it % 2 == 0
                }.onEach { //遍历每个数据
                    println(it)
                }.map { //map是对发射的数据做运算
                    it * it
                }.debounce(500L) //表示只有两条数据之间的间隔超过500毫秒才能发送成功。
                    .collect {//collect数据,终端操作符,也可以用collectLatest收集最新的数据,前一个还未处理完的数据就会中端处理直接处理最新的一个
                        println(it)
                    }
            }
            //复杂一点的操作符
            runBlocking {
                val flow = flow<Int> {
                    emit(1)
                    delay(500)
                    emit(2)
                    emit(3)
                    delay(600)
                    emit(4)
                }
                flow
                    .debounce(500L) //表示只有两条数据之间的间隔超过500毫秒才能发送成功。1和2之间隔了500,没有超过500,所以2发射失败,到发射3的时候,已经超过500,所以3可以发射成功,4是最后一个,所以4可以成功,但是我不理解为什么4是最后一个就可以发射成功
                    .sample(1000L)//表示一秒钟之内只允许发送一个数据
                    .collect {//collect数据,终端操作符,也可以用collectLatest收集最新的数据,前一个还未处理完的数据就会中端处理直接处理最新的一个
                        println(it)
                    }
            }
            //前面几个操作符都是最终需要collect函数来收集结果,下面的操作符不需要借助collect函数,自己就可以终结flow流程
            runBlocking {
                val flow = flow<Int> {
                    for (i in 1..100) {
                        emit(i)
                    }
                }
                //reduce函数会通过参数给我们一个Flow的累积值和一个Flow的当前值，我们可以在函数体中对它们进行一定的运算，运算的结果会作为下一个累积值继续传递到reduce函数当中
                val result = flow.reduce { accumulator, value ->
                    //这里需要注意的是，reduce函数是一个终端操作符函数，它的后面不可以再接其他操作符函数了，而是只能获取最终的运行结果。
                    accumulator + value
                }
                println(result)//结果为5050
                //fold与reduce完全一样,不同的是fold需要传入初始值
                val result2 = flow.fold(1) { acc, value ->
                    acc + value
                }
                println(result2) //结果为5051,因为初始值是1
                val result3 = flow {
                    for (i in ('A'..'Z')) {
                        emit(i.toString())
                    }
                }.fold("English:") { acc, value ->
                    acc + value
                }
                println(result3) //通过fold可以实现字符串的拼接
            }

            //下面开始难的操作符,从3种以flatMap开头的操作符函数，
            // 分别是flatMapConcat、flatMapMerge和flatMapLatest,现在开始是两个flow进行操作
            runBlocking {
                flowOf("张", "李", "王", "赵", "朱") //首先这几个字符串会依次发送
                    .flatMapConcat {//flatMapConcat的应用场景:一个接口A请求参数来自于另一个接口B的结果,只需要将获取参数的接口B当第一个flow,接口A当flatMapConcat的应用场景内的flow,这样第一个flow接口拿到结果后参与第二个flow的发送数据,第二个接口得到的数据在collect中收集
                        flowOf(
                            it + 3,
                            it + 4,
                            it + 5,
                            it + 6,
                            it + 7
                        ) //这个flow也会依次发送与第一个flow拼接后的带数字的字符串,it就是来自第一个flow依次发送的字符串
                    }.collect {
                        //最终结果就是第一个flow中的字符串依次与第二个flow的所有值进行拼接,最终等得到值数量是两个flow的个数相乘后的结果
                        println(it)

                    }
            }
        }
    }
}