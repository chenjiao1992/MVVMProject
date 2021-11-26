package com.cj.chat.model

import com.cj.chat.im.scim.IMsgContentType
import com.cj.chat.im.scim.IMsgDirect
import com.cj.chat.im.scim.ISpeakType
import com.cj.chat.model.interfaces.IHashCode
import com.cj.chat.model.interfaces.IMessageContent
import com.cj.chat.model.interfaces.IMultiType

/**
 *  Create by chenjiao at  2021/11/25 17:31
 *  描述：
 */
class Message : IHashCode, IMultiType {
    /**
     * 数据库的message id
     */
    var id = 0L
    var mid = 0L
    var uid: String? = null
    var cid: String? = null
    var direct = IMsgDirect.UNKNOWN
    var status = 0
    var sendTime = 0L
    var sender: Member? = null
    var senderExtra: SenderExtraData = SenderExtraData()
    var messageContent: IMessageContent? = null
    var speakType = ISpeakType.UNKNOWN
    val contentType
        get() = messageContent?.getContentType() ?: IMsgContentType.UNKOWN

    override fun getHashCode(): Int {
        return when (contentType) {
            IMsgContentType.TEXT -> {
                if (speakType == ISpeakType.DESCRIPTION) {
                    HASH_CODE_DESCRIPTION
                } else {
                    if (direct == IMsgDirect.SEND) HASH_CODE_NORMAL_SEND else HASH_CODE_NORMAL_RECV
                }
            }
            IMsgContentType.IMAGE, IMsgContentType.VOICE -> {
                if (direct == IMsgDirect.SEND) HASH_CODE_NORMAL_SEND else HASH_CODE_NORMAL_RECV
            }
            else -> HASH_CODE_OTHERS
        }
    }

    companion object {
        const val HASH_CODE_NORMAL_SEND = 0
        const val HASH_CODE_NORMAL_RECV = 1
        const val HASH_CODE_DESCRIPTION = 2
        const val HASH_CODE_OTHERS = -1

    }

    override val multiType: IMsgContentType
        get() = messageContent?.getContentType() ?: IMsgContentType.UNKOWN

}

/**
 * data class ,用data修饰的class,编译器会在背后默默的生成equal,toString等方法,且必须至少有一个被var或者val修饰的主构函数
 */
data class SenderExtraData(var senderTitle: String = "", var senderAdminType: Int = 0) {
}
