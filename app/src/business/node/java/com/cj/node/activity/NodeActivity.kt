package com.cj.node.activity

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.cj.mvvmproject.BaseBindingActivity
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ActivityNodeBinding
import com.cj.node.WorkFlow
import com.cj.node.WorkNode

/**
 * Create by chenjiao
 * 描述:在app中经常需要我们在做完一件事之后再做另一件事,过后又需要在前面两件事中间再插入一件,按以前的方法插入就需要前两件事情的先后逻辑,如果很麻烦,所以有了WorkFlow,用来管理整个流程
 */
class NodeActivity : BaseBindingActivity<ActivityNodeBinding>() {
    private var mWorkFlow: WorkFlow? = null

    private fun startWorkFlow() {
        mWorkFlow = WorkFlow.Builder()
            .withNode(getNode(NODE_FIRST_ID))
            .withNode(getNode(NODE_SECOND_ID))
            .withNode(getNode(NODE_THIRD_ID))
            .create()
        mWorkFlow?.start()
    }

    private fun getNode(nodeId: Int): WorkNode {
        return WorkNode.build(nodeId) { current ->
            AlertDialog.Builder(this@NodeActivity)
                .setTitle("这是第一条有态度的广告:$nodeId")
                .setPositiveButton("我看完了", null)
                .setOnDismissListener { //仅仅只需关心自己是否完成，下一个节点会自动执行
                    current.onCompleted()
                }.create().show()
        }
    }

    companion object {
        private const val NODE_FIRST_ID = 1
        private const val NODE_SECOND_ID = 2
        private const val NODE_THIRD_ID = 3
    }

    override fun getLayoutId()=R.layout.activity_node

    override fun onConfig(arguments: Intent?) {
        startWorkFlow()
    }
}