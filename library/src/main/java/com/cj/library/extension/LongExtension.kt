package com.cj.library.extension

import kotlin.math.ceil

fun Long?.orZero(): Long = this ?: 0L

inline fun Long.ceilSeconds( maxDuration: Long): Long {
    val actualDurationSec = ceil(this / 1000F).toLong()
    return if (actualDurationSec > maxDuration) maxDuration else actualDurationSec
}