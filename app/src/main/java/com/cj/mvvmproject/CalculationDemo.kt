package com.cj.mvvmproject

import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 *  Create by chenjiao at 2021/3/5 0005
 *  描述：算法相关
 */
class CalculationDemo {
    /**
     * 广度优先
     */
    public fun printAllViewsRangle(root: View) {
        val linkList = LinkedList<View>()
        linkList.add(root)
        while (linkList.isNotEmpty()) {
            val fistView = linkList.removeFirst()
            printView(fistView)
            if (fistView is ViewGroup) {
                for (index in 0 until fistView.childCount) {
                    linkList.addLast(fistView.getChildAt(index))
                }
            }
        }
    }

    /**
     * 深度优先
     */
    public fun printAllViewsDeep(root: View) {
        val linkedList = LinkedList<View>()
        linkedList.add(root)
        while (linkedList.isNotEmpty()){
            val firstView = linkedList.removeFirst()
            printView(firstView)
            if(firstView is ViewGroup){
                for(index in 0..firstView.childCount){
                    val childAt = firstView.getChildAt(index)
                    linkedList.addFirst(childAt)
                }
            }
        }
    }

    fun printView(view: View) {

    }
}