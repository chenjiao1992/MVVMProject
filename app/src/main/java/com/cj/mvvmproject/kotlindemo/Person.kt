package com.cj.mvvmproject.kotlindemo

import android.content.Intent

/**
 *  Create by chenjiao at 2021/2/25 0025
 *  描述：
 */
open class Person(var name: String?, var age: Int?, var sex: String?) {
    init {
        name = "尊敬的$name"
        age = age?.plus(1)
    }

    constructor(name: String, age: Int) : this(name, age, "男") {
        start { putExtra("ddd", "ddd") }
        appStr { x, str ->
            "$x" + str
        }
    }

    constructor(name: String) : this(name, 0) {

    }

    //参数为普通lambda
    fun appStr(cup: (Int, String) -> String) {
        cup.invoke(6, "幼儿园")
    }

    //参数为带接受者的lambda
    fun start(action: Intent.() -> Unit) {

    }

    /**
     * 泛型写在方法名之前
     */
    fun <T> initAnimal(param: T) {}

    /**
     * 泛型约束:父类型是泛型类
     */
    fun <T : Animal<T>> initAnimal1(param: T) {}

    /**
     * 泛型约束:父类型是非泛型类
     */
    fun <T : Person> initAnimal2(param: T) {}

    /**
     * 多个泛型约束:类约束只能有一个,接口可以有多少
     */
    fun <T> initAnimal3(param:T):String
            where T:Animal<T> ,T:IAnimal<T>,T:IPerson
    {return ""}



}