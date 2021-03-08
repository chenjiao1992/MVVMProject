package com.cj.mvvmproject;

import java.util.ArrayList;

/**
 Create by chenjiao at 2021/3/5 0005
 描述：实现二叉树
 */
class BinTree<T extends Comparable<T>> {
    private ArrayList<TreeNode> mTreeNodeList = new ArrayList<TreeNode>();
    //根节点
    private TreeNode<T> root;

    TreeNode<T> put(T data) {
        TreeNode<T> current = new TreeNode(data);
        if (mTreeNodeList.isEmpty()) {
            root = current;
        } else {
            TreeNode<T> left = root.left;
            TreeNode<T> right = root.right;
            int result = root.data.compareTo(current.data);
            if (result == -1) {
                if (left != null) {
                    int i = current.data.compareTo(left.data);

                } else {
                    root.left = current;
                    current.parent = root;
                }
            } else if (result == 0) {
            } else {
            }


            //            for (TreeNode treeNode : mTreeNodeList) {
            //                int result = treeNode.data.compareTo(current.data);
            //                if (result == -1) {
            //
            //                } else if (result == 0) {
            //                } else {
            //                }
            //            }

        }
        mTreeNodeList.add(current);
        return current;
    }

    void reSetBinTree(TreeNode<T> first, TreeNode<T> secont) {
        TreeNode<T> left = first.left;
        TreeNode<T> right = first.right;
        int result = first.data.compareTo(secont.data);
        if (result == -1) {
            if (left != null) {
                reSetBinTree(left, secont);
            } else {
                first.left = secont;
                secont.parent = first;
            }
        } else if (result == 0) {
            TreeNode<T> temp = left;
            first.left = secont;
            secont.left = temp;
        } else {
            if (right != null) {
                reSetBinTree(right, secont);
            } else {
                first.right = secont;
                secont.parent = first;
            }
        }
    }

    class TreeNode<T extends Comparable<T>> {
        TreeNode<T> left;
        TreeNode<T> right;
        TreeNode<T> parent;
        T data;

        public TreeNode(T data) {
            this.data = data;
        }
    }
}
