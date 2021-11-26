package com.cj.chat.model

import com.cj.chat.im.scim.INotifyContentType
import com.cj.chat.model.interfaces.IHashCode
import com.cj.chat.model.interfaces.INotifyContent

class NotifyMsg : IHashCode {
    var uid: String? = null
    var cid: String? = null
    var sender: Member? = null
    var senderAccountId: String? = null
    var notifyTime: Long = 0
    var notifyContent: INotifyContent? = null
    val notifyType
        get() = notifyContent?.getContentType()

    override fun getHashCode(): Int {
        return when (notifyType) {
            INotifyContentType.STATUS -> HASH_CODE_STATUS
            else -> -1
        }
    }

    companion object {
        const val HASH_CODE_STATUS = 1001
    }
}
