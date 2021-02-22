package com.cj.library.recyclerview

import android.view.View
import androidx.databinding.ViewDataBinding

/**
 *  Create by chenjiao at 2021/2/22 0022
 *  描述：
 */
abstract class SimpleBindingHolder<ITEM : Any>(itemView: View) : CommonBindingViewHolder<ITEM, ViewDataBinding>(itemView)