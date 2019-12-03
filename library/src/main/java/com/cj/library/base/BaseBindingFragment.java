package com.cj.library.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 Create by chenjiao at 2019/12/3 0003
 描述：
 */
public abstract class BaseBindingFragment<T extends ViewDataBinding> extends BaseFragment {
    private static final String TAG = "BaseBindingFragment";
    protected T mBinding;

    protected abstract int getLayoutId();

    @Override
    public final View doInflate(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        Log.d("BaseBindingFragment", "mBinding=" + mBinding);
        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        mBinding.unbind();
        mBinding = null;
        super.onDestroyView();
    }
}
