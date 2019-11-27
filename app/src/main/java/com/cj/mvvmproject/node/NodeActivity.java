package com.cj.mvvmproject.node;

import android.app.Activity;
import android.os.Bundle;

import com.cj.mvvmproject.R;

import androidx.annotation.Nullable;

/**
 Create by chenjiao at 2019/11/26 0026
 描述：
 */
public class NodeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node);
        startWorkFlow();
    }

    private void startWorkFlow() {

    }
}
