package com.cj.mvvmproject;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 Create by chenjiao at 2019/12/6 0006
 描述：屏幕适配方案demo
 */
public class ScreenAdapterActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_adapter);
    }
}
