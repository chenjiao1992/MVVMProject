package com.cj.mvvmproject.fragment;

import android.os.Bundle;

import com.cj.library.base.BaseBindingFragment;
import com.cj.mvvmproject.R;
import com.cj.mvvmproject.databinding.FragmentMainBinding;

/**
 Create by chenjiao at 2019/11/26 0026
 描述：
 */
public class MainFragment extends BaseBindingFragment<FragmentMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onConfig(Bundle arguments) {

    }

    @Override
    public void onPrepared() {

    }
}
