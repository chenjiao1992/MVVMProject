package com.cj.mvvmproject.node;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import com.cj.mvvmproject.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

/**
 Create by chenjiao at 2019/11/26 0026
 描述:在app中经常需要我们在做完一件事之后再做另一件事,过后又需要在前面两件事中间再插入一件,按以前的方法插入就需要前两件事情的先后逻辑,如果很麻烦,所以有了WorkFlow,用来管理整个流程
 */
public class NodeActivity extends Activity {
    private static final int NODE_FIRST_ID = 1;
    private static final int NODE_SECOND_ID = 2;
    private static final int NODE_THIRD_ID = 3;
    private WorkFlow mWorkFlow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
        startWorkFlow();
    }

    private void startWorkFlow() {
        mWorkFlow = new WorkFlow.Builder()
                .withNode(getNode(NODE_FIRST_ID))
                .withNode(getNode(NODE_SECOND_ID))
                .withNode(getNode(NODE_THIRD_ID))
                .create();
        mWorkFlow.start();
    }

    private WorkNode getNode(final int nodeId) {
        return WorkNode.build(nodeId, new Worker() {

            @Override
            public void doWork(final Node current) {
                new AlertDialog.Builder(NodeActivity.this)
                        .setTitle("这是第一条有态度的广告:" + nodeId)
                        .setPositiveButton("我看完了", null)
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                //仅仅只需关心自己是否完成，下一个节点会自动执行
                                current.onCompleted();
                            }
                        }).create().show();
            }
        });
    }


}
