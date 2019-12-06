package com.cj.library.extension

import kotlin.math.ceil

fun Int?.orZero(): Int = this ?: 0

fun Int?.indent(size: Int, suffix: Char) = indent(size, suffix.toString())

fun Int?.indent(size: Int, suffix: String) = if (this == null) {
    "0"
} else {
    if (this > size) {
        "${(this / size.toFloat()).toInt()}$suffix"
    } else {
        "$this"
    }
}

fun Int?.indentFloat(size: Int, suffix: Char) = indentFloat(size, suffix.toString())

fun Int?.indentFloat(size: Int, suffix: String) = if (this == null) {
    "0"
} else {
    if (this > size) {
        val result = this / size.toFloat()
        if (result < 10) {
            val remainder = (result * 10).toInt() % 10
            "${result.toInt()}.$remainder$suffix"
        } else "${result.toInt()}$suffix"
    } else {
        "$this"
    }
}

fun Int?.unReadFormat(max: Int) = if (this.orZero() > max) "$max+" else "$this"

inline fun Int.ceilSeconds( maxDuration: Int= Int.MAX_VALUE): Int {
    val actualDurationSec = ceil(this / 1000F).toInt()
    return if (actualDurationSec > maxDuration) maxDuration else actualDurationSec
}