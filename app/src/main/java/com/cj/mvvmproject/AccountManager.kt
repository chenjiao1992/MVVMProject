package com.cj.mvvmproject

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.cj.library.utils.PreferenceUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * 由 Harreke 创建于 2017/9/30.
 */
object AccountManager {

    var themeName = ""
        get() {
            if (field.isEmpty()) {
                field = readAccountString("themeName", "").orEmpty()
            }
            return field
        }
        set(value) {
            if (field == value) return
            field = value
            writeAccountString("themeName", value)
        }

    @SuppressLint("ConstantLocale")
    private val mDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun readSettings(name: String, action: (SharedPreferences) -> Unit) {
        PreferenceUtil.read(name, action)
    }

    fun writeAccountString(key: String, value: String?) {
        PreferenceUtil.writeString("accountId" ?: return, key, value)
    }

    fun readAccountString(key: String, defaultValue: String? = null): String? {
        val prefAccount = "accountId" ?: return defaultValue
        return PreferenceUtil.readString(prefAccount, key, defaultValue)
    }

    fun writeSettings(name: String, action: (SharedPreferences.Editor) -> Unit) {
        PreferenceUtil.write(name, action)
    }

    fun removeSettings(name: String, vararg keys: String) {
        PreferenceUtil.remove(name, *keys)
    }

}