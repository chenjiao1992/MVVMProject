package com.cj.chat.model

import com.cj.chat.im.scim.IMsgContentType
import com.cj.chat.model.interfaces.IMultiType
import com.cj.chat.model.interfaces.ITarget
import com.cj.chat.model.interfaces.IUserTarget

/**
 *  Create by chenjiao at  2021/11/25 17:38
 *  描述：
 */
class Member : User(), IUserTarget, IMultiType {
    /**
     * 皮id 名人id_皮编号
     */
    var rid: Long = 0
    var number = 0L
    var memberId: String? = null
        get() {
            if (field.isNullOrEmpty()) {
                field = "${id}_${number}"

            }
            return field
        }
    var deviceId: String? = null
    override val multiType: IMsgContentType
        get() = subType
    override val mine = false
    override var subType = IMsgContentType.UNKOWN
    override var coreId: String
        get() = memberId.orEmpty()
        set(value) {
            memberId = value
            val ids = value.split("_")
            id = ids[0]
            number = ids[1].toLong()
        }
    override var name: String = nickName.orEmpty()
    override var alias: String
        get() = "$nickName($number)"
        set(value) {}

    override fun <T : ITarget> copyFrom(other: T) {
        if (other !is Member) return
        id = other.id
        number = other.number
        nickName = other.nickName
        avatar = other.avatar
        subType = other.subType
        coreId = other.coreId
        name = other.name
    }

    override fun <T : ITarget> isSame(other: T) = other is Member && other.coreId == coreId
}