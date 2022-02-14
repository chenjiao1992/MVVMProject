package com.cj.mvvmproject.annotaion

import com.cj.mvvmproject.annotaion.CustomTag
import kotlin.jvm.JvmStatic
import com.cj.mvvmproject.annotaion.CustonTagAnotationManager
import com.cj.mvvmproject.annotaion.AnotationDemo
import java.lang.Exception

/**
 * Create by chenjiao at 2021/3/9 0009
 * 描述：
 */
object CustonTagAnotationManager {
    fun process(className: String?) {
        try {
            val aClass = Class.forName(className!!)
            val info = aClass.getDeclaredMethod("info", null)
            val annotation1 = info.getAnnotation(CustomTag::class.java)
            print("CustonTagAnotationManager" + annotation1.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        process(AnotationDemo::class.java.name)
    }
}