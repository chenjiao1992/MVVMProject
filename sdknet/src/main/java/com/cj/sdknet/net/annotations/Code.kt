package com.cj.sdknet.net.annotations

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * 描述：用来申明网络请求返回数据的解析规则
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Code(val value: String = "error")
