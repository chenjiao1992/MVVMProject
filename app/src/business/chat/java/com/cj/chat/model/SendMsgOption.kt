package com.cj.chat.model

import com.cj.chat.model.interfaces.ISendCallBack

class SendMsgOption {
    var sendBroadcast = false
    var sendCallBack: ISendCallBack? = null
    var recordUnread = false
    var isRevoke = false

    companion object {
        fun createDefault(): SendMsgOption {
            return SendMsgOption().apply {
                sendBroadcast = false
                sendCallBack = null
                recordUnread = false
                isRevoke = false
            }
        }

        fun createRevoke(): SendMsgOption {
            return SendMsgOption().apply {
                sendBroadcast = false
                sendCallBack = null
                recordUnread = false
                isRevoke = true
            }
        }
    }
}
