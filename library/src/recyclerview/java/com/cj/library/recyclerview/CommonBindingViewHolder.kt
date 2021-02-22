package com.cj.library.recyclerview

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *  Create by chenjiao at 2021/2/22 0022
 *  描述：ViewHoler
 *  抽象类的泛型可以比父类要少写
 */
abstract class CommonBindingViewHolder<in ITEM:Any,BINDING : ViewDataBinding?>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = DataBindingUtil.bind<BINDING>(itemView)!!

    fun setVariable(variableId: Int, value: Any?) {
        binding?.setVariable(variableId, value)
    }

}