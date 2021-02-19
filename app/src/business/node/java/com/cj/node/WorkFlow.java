package com.cj.node;

import android.util.SparseArray;

/**
 Create by chenjiao
 描述：管理整个流程的所有节点的执行顺序
 */
public class WorkFlow {
    private SparseArray<WorkNode> mFlowNodes;

    private WorkFlow(SparseArray<WorkNode> nodes) {
        mFlowNodes = nodes;
    }

    public void start() {
        startWithNode(mFlowNodes.keyAt(0));
    }

    private void startWithNode(int startNodeId) {
        final int index = mFlowNodes.indexOfKey(startNodeId);
        WorkNode workNode = mFlowNodes.valueAt(index);
        workNode.doWork(new WorkNode.WorkCallBack() {
            @Override
            public void onWorkCompleted() {
                findAndExecuteNextNode(index);
            }
        });

    }

    private void findAndExecuteNextNode(int startIndex) {
        final int currentIndex = startIndex + 1;
        WorkNode workNode = mFlowNodes.valueAt(currentIndex);
        if (workNode != null) {
            workNode.doWork(new WorkNode.WorkCallBack() {
                @Override
                public void onWorkCompleted() {
                    findAndExecuteNextNode(currentIndex);
                }
            });
        }
    }

    public static class Builder {
        private SparseArray<WorkNode> mFlowNodes;

        public Builder() {
            mFlowNodes = new SparseArray<>();
        }

        public Builder withNode(WorkNode node) {
            this.mFlowNodes.append(node.getId(), node);
            return this;
        }

        public WorkFlow create() {
            return new WorkFlow(mFlowNodes);
        }
    }
}
