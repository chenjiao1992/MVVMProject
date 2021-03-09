package com.cj.mvvmproject.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 Create by chenjiao at 2021/3/9 0009
 描述：Retention 保留的时间
 Target作用哪些程序单元
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CustomTag {
    String name() default "";
    String vision() default "0.0";
}
