package com.cj.mvvmproject.fragment;

import android.os.Bundle;

import com.cj.library.base.BaseBindingFragment;
import com.cj.mvvmproject.R;
import com.cj.mvvmproject.databinding.FragmentDemoBinding;

/**
 Create by chenjiao at 2019/12/3 0003
 描述：
 */
public class DemoFragment extends BaseBindingFragment<FragmentDemoBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_demo;
    }

    @Override
    public void onConfig(Bundle arguments) {
      mBinding.tvContent.setText("我是内容");
    }

    @Override
    public void onPrepared() {
    }
}
