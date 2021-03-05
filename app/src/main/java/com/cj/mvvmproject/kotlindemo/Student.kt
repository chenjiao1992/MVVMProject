package com.cj.mvvmproject.kotlindemo

import com.cj.mvvmproject.kotlindemo.Person

/**
 *  Create by chenjiao at 2021/2/25 0025
 *  描述：
 */
class Student(name: String, age: Int) : Person(name, age) {
    val sum: (Int, Int) -> Int = { x, y ->  x + y }

}