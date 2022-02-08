package com.cj.im

/**
 * 由 Harreke 创建于 2017/11/14.
 */
fun Boolean.trueOrFalse(trueVale: Int, falseValue: Int) = if (this) trueVale else falseValue

fun Boolean.trueOrFalse(trueVale: String, falseValue: String) = if (this) trueVale else falseValue

fun Boolean.trueOrFalse(trueVale: Float, falseValue: Float) = if (this) trueVale else falseValue

fun Boolean.trueOrFalse(trueVale: Boolean, falseValue: Boolean) = if (this) trueVale else falseValue

fun Boolean.trueOrFalse(trueVale: Long, falseValue: Long) = if (this) trueVale else falseValue

fun Boolean?.orFalse(): Boolean = this ?: false

fun Boolean?.orTrue(): Boolean = this ?: true

fun Boolean?.toBinary() = if (this == null || !this) 0 else 1