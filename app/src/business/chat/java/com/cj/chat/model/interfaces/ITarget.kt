package com.cj.chat.model.interfaces

import com.cj.chat.im.scim.IMsgContentType

/**
 *  Create by chenjiao at  2021/11/25 17:43
 *  描述：
 */
interface ITarget {
    /**
     * 子类型,例如Team类型可分为群和讨论组
     */
    var subType: IMsgContentType

    /**
     * 对象id
     */
    var coreId: String

    /**
     * 对象名称
     */
    var name: String

    /**
     * 别名
     */
    var alias: String

    /**
     * 对象头像
     */
    var avatar: String?

    /**
     * 数据拷贝
     */
    fun <T : ITarget> copyFrom(other: T)

    /**
     * 判断是否完全相同
     */
    fun <T : ITarget> isSame(other: T): Boolean
}