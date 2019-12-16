package com.cj.library.extension

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

fun String?.notNullNorEmpty() = this != null && this.isNotEmpty()

fun String?.notNullNorBlank() = this != null && this.isNotBlank()

fun String?.urlEncode() = if (this == null) null else try {
    URLEncoder.encode(this, "UTF-8")
} catch (e: UnsupportedEncodingException) {
    null
}

fun String?.nullOrEmpty(defaultValue: String) = if (this.isNullOrEmpty()) defaultValue else this

fun String?.urlEncode(defaultValue: String): String = if (this == null) defaultValue else try {
    URLEncoder.encode(this, "UTF-8")
} catch (e: UnsupportedEncodingException) {
    defaultValue
}

fun String?.toLongSafe() = try {
    this?.toLong().orZero()
} catch (e: Exception) {
    0L
}

fun String?.toIntSafe() = try {
    this?.toInt().orZero()
} catch (e: Exception) {
    0
}

fun CharSequence.findLastNumberIndex(): Int {
    if (this.isEmpty()) return -1
    if (this.length == 1) return if (this.first() in '0'..'9') 0 else -1
    this.reduceRightIndexed { index, c, acc ->
        if (acc !in '0'..'9') return -1
        if (c !in '0'..'9') return index + 1
        c
    }
    return 0
}

/**
 * Returns a new string with the last occurrence of [oldChar] replaced with [newChar].
 */
fun String.replaceLast(oldValue: String, newValue: String, ignoreCase: Boolean = false): String {
    val index = this.lastIndexOf(oldValue, ignoreCase = ignoreCase)
    return if (index < 0) this else this.replaceRange(index, index + oldValue.length, newValue)
}

fun SpannableStringBuilder.addSpan(text: String, vararg spans: Any): SpannableStringBuilder {
    if (text.isEmpty()) return this
    val preLength = length
    append(text)
    val nowLength = length
    spans.forEach {
        setSpan(it, preLength, nowLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return this
}

fun SpannableStringBuilder.addSpan(text: String, spans: List<Any>): SpannableStringBuilder {
    if (text.isEmpty()) return this
    val preLength = length
    append(text)
    val nowLength = length
    spans.forEach {
        setSpan(it, preLength, nowLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return this
}

fun SpannableStringBuilder.addImage(placeholderText: String, image: Drawable, verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BOTTOM) =
        addSpan(placeholderText, ImageSpan(image, verticalAlignment))

fun SpannableStringBuilder.addForegroundColorText(text: String, color: Int) =
        addSpan(text, ForegroundColorSpan(color))

fun SpannableStringBuilder.addBackgroundColorText(text: String, color: Int) =
        addSpan(text, BackgroundColorSpan(color))

fun SpannableStringBuilder.addColorText(text: String, foregroundColor: Int, backgroundColor: Int) =
        addSpan(text, ForegroundColorSpan(foregroundColor), BackgroundColorSpan(backgroundColor))

fun SpannableStringBuilder.insertSpan(where: Int, text: String, vararg spans: Any): SpannableStringBuilder {
    if (text.isEmpty()) return this
    insert(where, text)
    val nowLength = where + text.length
    spans.forEach {
        setSpan(it, where, nowLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return this
}

fun SpannableStringBuilder.insertImage(where: Int, placeholderText: String, image: Drawable, verticalAlignment: Int = DynamicDrawableSpan.ALIGN_BASELINE) =
        insertSpan(where, placeholderText, ImageSpan(image, verticalAlignment))

fun SpannableStringBuilder.insertForegroundColorText(where: Int, text: String, color: Int) =
        insertSpan(where, text, ForegroundColorSpan(color))

fun SpannableStringBuilder.insertBackgroundColorText(where: Int, text: String, color: Int) =
        insertSpan(where, text, BackgroundColorSpan(color))

fun SpannableStringBuilder.insertColorText(where: Int, text: String, foregroundColor: Int, backgroundColor: Int) =
        insertSpan(where, text, ForegroundColorSpan(foregroundColor), BackgroundColorSpan(backgroundColor))

fun String?.excludeNonASCII(): String? {
    if (this == null || this.isEmpty()) return this
    val result = StringBuilder()
    for (char in this) {
        if ((char <= '\u001f' && char != '\t') || char >= '\u007f') continue
        result.append(char)
    }
    return result.toString()
}