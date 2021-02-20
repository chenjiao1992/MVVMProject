package com.cj.library.utils

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.cj.library.base.BaseApplicaton
import com.cj.library.extension.defaultSharedPreference
import com.cj.library.extension.getArray
import com.cj.library.extension.getList
import com.cj.library.extension.getObject
import com.cj.library.extension.putObject
import com.cj.library.extension.sharedPreference


/**
 * 由 Harreke 创建于 2017/9/27.
 */
@SuppressLint("ApplySharedPref")
object PreferenceUtil {
    fun getPreference(name: String? = null): SharedPreferences? {
        val application = BaseApplicaton.instance ?: return null
        return if (name.isNullOrEmpty()) {
            application.defaultSharedPreference()
        } else {
            application.sharedPreference(name)
        }
    }

    fun hasKey(name: String? = null, key: String) = getPreference(name)?.contains(key)

    fun readBoolean(name: String? = null, key: String, defaultValue: Boolean = false) = getPreference(name)?.getBoolean(key, defaultValue) ?: defaultValue

    fun readFloat(name: String? = null, key: String, defaultValue: Float = 0F) = getPreference(name)?.getFloat(key, defaultValue) ?: defaultValue

    fun readInt(name: String? = null, key: String, defaultValue: Int = 0) = getPreference(name)?.getInt(key, defaultValue) ?: defaultValue

    fun readLong(name: String? = null, key: String, defaultValue: Long = 0L) = getPreference(name)?.getLong(key, defaultValue) ?: defaultValue

    fun readString(name: String, key: String, defaultValue: String? = null) = getPreference(name)?.getString(key, defaultValue) ?: defaultValue

    fun readString(key: String, defaultValue: String? = null) = getPreference(null)?.getString(key, defaultValue) ?: defaultValue

    fun readStringSet(name: String? = null, key: String, defaultValue: Set<String>? = null): Set<String>? =
            getPreference(name)?.getStringSet(key, defaultValue) ?: defaultValue

    fun <T : Any> readObject(name: String? = null, key: String, clazz: Class<T>, defaultValue: T? = null) =
            getPreference(name)?.getObject(key, clazz) ?: defaultValue

    fun <T : Any> readArray(name: String? = null, key: String, clazz: Class<T>, defaultValue: Array<T>? = null) =
            getPreference(name)?.getArray(key, clazz) ?: defaultValue

    fun <T : Any> readList(name: String? = null, key: String, clazz: Class<T>, defaultValue: List<T>? = null) =
            getPreference(name)?.getList(key, clazz, defaultValue) ?: defaultValue

    fun writeBoolean(name: String? = null, key: String, value: Boolean) {
        getPreference(name)?.edit()?.putBoolean(key, value)?.commit()
    }

    fun writeFloat(name: String? = null, key: String, value: Float) {
        getPreference(name)?.edit()?.putFloat(key, value)?.commit()
    }

    fun writeInt(name: String? = null, key: String, value: Int) {
        getPreference(name)?.edit()?.putInt(key, value)?.commit()
    }

    fun writeLong(name: String? = null, key: String, value: Long) {
        getPreference(name)?.edit()?.putLong(key, value)?.commit()
    }

    fun writeString(name: String? = null, key: String, value: String?) {
        getPreference(name)?.edit()?.putString(key, value)?.commit()
    }

    fun writeString(key: String, value: String?) {
        getPreference(null)?.edit()?.putString(key, value)?.commit()
    }

    fun writeStringSet(name: String? = null, key: String, value: Set<String>?) {
        getPreference(name)?.edit()?.putStringSet(key, value)?.commit()
    }

    fun <T : Any> writeObject(name: String? = null, key: String, value: T?) {
        getPreference(name)?.edit()?.putObject(key, value)?.commit()
    }

    fun remove(name: String? = null, vararg keys: String) {
        val preferences = getPreference(name) ?: return
        val editor = preferences.edit()
        for (key in keys) {
            editor.remove(key)
        }
        editor.commit()
    }

    fun read(name: String? = null, action: (SharedPreferences) -> Unit) {
        val preferences = getPreference(name) ?: return
        action(preferences)
    }

    fun write(name: String? = null, action: (SharedPreferences.Editor) -> Unit) {
        val preferences = getPreference(name) ?: return
        val editor = preferences.edit()
        action(editor)
        editor.commit()
    }
}