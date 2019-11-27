package com.cj.mvvmproject.node;

/**
 Create by chenjiao at 2019/11/26 0026
 描述：
 */
public class WorkNode implements Node {
    private int mNodeId;
    private Worker mWorker;
    private WorkCallBack mWorkCallBack;

    private WorkNode(int nodeId, Worker worker) {
        this.mNodeId = nodeId;
        this.mWorker = worker;
    }

    public static WorkNode build(int nodeId, Worker worker) {
        return new WorkNode(nodeId, worker);
    }

    void doWork(WorkCallBack workCallBack) {
        this.mWorkCallBack = workCallBack;
        mWorker.doWork(this);
    }

    @Override
    public int getId() {
        return mNodeId;
    }

    @Override
    public void onCompleted() {
        if (null != mWorkCallBack) {
            mWorkCallBack.onWorkCompleted();
        }
    }


    interface WorkCallBack {
        /**
         当前任务完成
         */
        void onWorkCompleted();
    }
}
