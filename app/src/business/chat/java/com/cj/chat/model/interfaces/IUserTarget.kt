package com.cj.chat.model.interfaces

/**
 *  Create by chenjiao at  2021/11/25 17:48
 *  描述：皮对象
 */
interface IUserTarget : ITarget {
    /**
     * 是否为自己账号下的对象
     */
    val mine: Boolean
}