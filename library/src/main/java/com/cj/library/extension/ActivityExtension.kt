package com.cj.library.extension

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import java.io.File
import java.io.FileInputStream
import java.util.*

/**
 * 由 Harreke 创建于 2018/10/12.
 */
fun Activity.setResult(resultCode: Int, fillAction: Intent.() -> Unit) {
    val data = Intent()
    data.apply(fillAction)
    setResult(resultCode, data)
}

fun Activity.finish(resultCode: Int) {
    setResult(resultCode)
    finish()
}

fun Activity.finish(resultCode: Int, fillAction: Intent.() -> Unit) {
    val data = Intent()
    fillAction(data)
    setResult(resultCode, data)
    finish()
}

fun Activity.finish(resultCode: Int, data: Intent?) {
    setResult(resultCode, data)
    finish()
}

fun Activity.finishWhenOK(resultCode: Int, data: Intent?) {
    setResult(resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
        finish()
    }
}

fun Activity.hideInputMethod() {
    (applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(window.decorView.windowToken, 0)
}

fun Activity.showInputMethod() {
    (applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(window.decorView, 0)
}


/**
 * 设置状态栏为亮色模式
 * 亮色模式=状态栏文字为黑色
 * 非亮色模式=状态栏文字为白色
 */
fun Activity.setStatusBarLightMode(lightMode: Boolean) {
    val window = window ?: return
    if (isFlyme()) {
        processFlyme(window, lightMode)
    } else if (isMIUI()) {
        processMIUI(window, lightMode)
    } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            val systemUiVisibility = decorView.systemUiVisibility
            if (lightMode) {
                decorView.systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }
}

private fun processFlyme(window: Window, lightMode: Boolean) {
    val params = window.attributes
    try {
        val clazz = Class.forName("android.view.WindowManager\$LayoutParams")
        val value = clazz.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON").getInt(params)
        val field = clazz.getDeclaredField("meizuFlags")
        field.isAccessible = true
        val origin = field.getInt(params)
        if (lightMode) {
            field.set(params, origin or value)
        } else {
            field.set(params, origin and value.inv())
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("PrivateApi")
private fun processMIUI(window: Window, lightMode: Boolean) {
    try {
        val clazz = window::class.java
        val paramsClazz = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
        val field = paramsClazz.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
        val flags = field.getInt(paramsClazz)
        val method = clazz.getMethod("setExtraFlags", Int::class.java, Int::class.java)
        method.invoke(window, if (lightMode) flags else 0, flags)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private val mProperties: Properties by lazy {
    val field = Properties()
    field.load(FileInputStream(File(Environment.getRootDirectory(), "build.prop")))
    field
}
const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
const val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

private fun isMIUI() = try {
    val properties = mProperties
    (properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
            || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
            || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null)
} catch (e: Exception) {
    false
}

private fun isFlyme() = try {
    Build::class.java.getMethod("hasSmartBar")
    true
} catch (e: Exception) {
    false
}
