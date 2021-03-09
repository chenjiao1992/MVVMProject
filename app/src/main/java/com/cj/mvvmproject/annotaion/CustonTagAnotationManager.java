package com.cj.mvvmproject.annotaion;

import android.util.Log;

import java.lang.reflect.Method;

/**
 Create by chenjiao at 2021/3/9 0009
 描述：
 */
public class CustonTagAnotationManager {
    public static void process(String className){
       try{
           Class<?> aClass = Class.forName(className);
           Method info = aClass.getDeclaredMethod("info", null);
           CustomTag annotation1 = info.getAnnotation(CustomTag.class);
           System.out.print("CustonTagAnotationManager"+annotation1.name());
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public static void main(String[]  args){
       process(AnotationDemo.class.getName());
    }
}
