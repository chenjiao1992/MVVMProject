package com.cj.im.model.bean

/**
 *  Create by chenjiao at  2022/2/7 11:19
 *  描述：
 */
interface ITarget {
    /**
     * 对象id
     */
    var id:Long

    /**
     * 对象子类型
     */
    var type:Int

    /**
     * 对象名称
     */
    var name:String

    /**
     * 对象头像
     */
    var avatar:String?
}