package com.cj.library.base;

import android.app.Application;

import com.cj.library.utils.MetricsUtil;

/**
 Create by chenjiao at 2019/12/6 0006
 描述：
 */
public class BaseApplicaton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MetricsUtil.INSTANCE.init(this);
    }
}
