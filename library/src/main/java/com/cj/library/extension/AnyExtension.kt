package com.cj.library.extension

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Closeable
import java.net.Socket

fun Closeable?.closeSafe() {
    try {
        this?.close()
    } catch (e: Exception) {
    }
}

fun Socket?.closeSafe() {
    try {
        this?.close()
    } catch (e: Exception) {
    }
}

fun Any?.asContext() = when {
    this == null -> null
    this is Activity -> this
    this is Fragment -> context
    this is View -> context
    else -> null
}

fun Any?.getFragmentManager() = when {
    this == null -> null
    this is FragmentActivity -> supportFragmentManager
    this is Fragment -> childFragmentManager
    else -> null
}

inline fun <reified P : Parcelable> Bundle.putParcelableAs(key: String, value: Parcelable?): Bundle {
    if (value == null) return this
    val argumentClassName = value::class.java.canonicalName
    val requiredClassName = P::class.java.canonicalName
    if (argumentClassName != requiredClassName) {
        throw IllegalArgumentException("参数类型($argumentClassName)与要求类型($requiredClassName)不符！")
    }
    putParcelable(key, value)
    return this
}

inline fun <reified P : Parcelable> Intent.putParcelableAs(key: String, value: Parcelable?): Intent {
    if (value == null) return this
    val argumentClassName = value::class.java.canonicalName
    val requiredClassName = P::class.java.canonicalName
    if (argumentClassName != requiredClassName) {
        throw IllegalArgumentException("参数类型($argumentClassName)与要求类型($requiredClassName)不符！")
    }
    putExtra(key, value)
    return this
}