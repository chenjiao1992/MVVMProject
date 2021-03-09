package com.cj.mvvmproject.enumdemo;

import com.cj.mvvmproject.enumdemo.Color;

/**
 Create by chenjiao at 2021/3/9 0009
 描述：
 */
class ColorDemo {

    //获取所有枚举常量
    private void getColors() {
        Color[] colors = Color.values();
        //获取单个枚举常量
        Color color = Color.GREEN;
        int ordinal = color.ordinal();
    }
}
