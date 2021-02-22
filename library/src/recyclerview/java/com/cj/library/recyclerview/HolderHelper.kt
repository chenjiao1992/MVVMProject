package com.cj.library.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

/**
 *  Create by chenjiao at 2021/2/22 0022
 *  描述：
 */
open class HolderHelper {
    companion object {
        public fun createHolder(parent: ViewGroup, layoutId: Int): SimpleBindingDelegateHolder<ViewDataBinding> {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(layoutId, parent, false)
            return SimpleBindingDelegateHolder(itemView)

        }
    }
}