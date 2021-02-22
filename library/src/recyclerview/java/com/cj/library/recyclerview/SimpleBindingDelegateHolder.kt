package com.cj.library.recyclerview

import android.view.View
import androidx.databinding.ViewDataBinding

/**
 *  Create by chenjiao at 2021/2/22 0022
 *  描述：
 */
open  class SimpleBindingDelegateHolder<ITEM : Any>(itemView: View,
        initiator: ((SimpleBindingHolder<ITEM>, binding: ViewDataBinding) -> Unit)?,
        private val binder: ((binding: ViewDataBinding, item: ITEM) -> Unit)?) : SimpleBindingHolder<ITEM>(itemView) {
    constructor(itemView: View) : this(itemView, null, null)

    init {
        initiator?.invoke(this, binding)
    }
}