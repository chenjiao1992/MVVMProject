package com.cj.library.extension

import android.content.SharedPreferences

/**
 * 由 Harreke 创建于 2018/3/6.
 */
fun <T : Any> SharedPreferences.getObject(key: String, clazz: Class<T>, defaultValue: T? = null) =
        getString(key, null).toObject(clazz) ?: defaultValue

fun <T : Any> SharedPreferences.getArray(key: String, clazz: Class<T>, defaultValue: Array<out T>? = null) =
        getString(key, null).toArray(clazz) ?: defaultValue

fun <T : Any> SharedPreferences.getList(key: String, clazz: Class<T>, defaultValue: List<T>? = null) =
        getString(key, null)?.toList(clazz) ?: defaultValue

fun <T : Any> SharedPreferences.Editor.putObject(key: String, value: T?): SharedPreferences.Editor = putString(key, value.toJson())