package com.cj.screen.activity;

import android.content.Intent;

import com.cj.mvvmproject.BaseBindingActivity;
import com.cj.mvvmproject.R;
import com.cj.mvvmproject.databinding.ActivityScreenAdapterBinding;

import org.jetbrains.annotations.NotNull;

/**
 Create by chenjiao at 2019/12/6 0006
 描述：屏幕适配方案demo
 */
public class ScreenAdapterActivity extends BaseBindingActivity<ActivityScreenAdapterBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_screen_adapter;
    }




    @Override
    protected void onConfig(@NotNull Intent arguments) {

    }
}
