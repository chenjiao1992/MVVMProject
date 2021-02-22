package com.cj.recyclerview.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.cj.library.recyclerview.HolderHelper
import com.cj.library.recyclerview.SimpleBindingDelegateHolder
import com.cj.mvvmproject.BR
import com.cj.mvvmproject.R
import com.cj.mvvmproject.databinding.ItemSimpBinding
import com.cj.recyclerview.FunctionBean

/**
 *  Create by chenjiao at 2021/2/22 0022
 *  描述：
 */
class DemoAdapter(private var mList: ArrayList<FunctionBean>) :
        RecyclerView.Adapter<SimpleBindingDelegateHolder<ViewDataBinding>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HolderHelper.createHolder(parent, R.layout.item_simp)

    override fun getItemCount() = mList.size

    override fun onBindViewHolder(holder: SimpleBindingDelegateHolder<ViewDataBinding>, position: Int) {
        val binding = holder.binding as ItemSimpBinding
        binding.setVariable(BR.functionBean, mList[position])
    }

}