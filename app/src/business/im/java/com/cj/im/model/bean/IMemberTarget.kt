package com.cj.im.model.bean

import android.os.Parcelable

/**
 *  Create by chenjiao at  2022/2/7 11:17
 *  描述：对象接口基类
 *  对象可能是用户(User),群(Team),也可能是其他任何业务层自行定义的类型
 */
interface IMemberTarget : ITarget, Parcelable {
    /**
     * 别名
     */
    var alias: String?

    /**
     * 对象头像框
     */
    var frame: String?

    /**
     * 是否是自己
     */
    var mine: Boolean?

    /**
     * 将本目标数据覆盖成指定目标的数据
     *
     * 用于进行数据拷贝，但改变原来的object引用关系
     */
    fun copyFrom(other: IMemberTarget)

    /**
     * 判断本目标是否与指定目标的数据完全相同
     */
    fun isSame(other: IMemberTarget?): Boolean

}