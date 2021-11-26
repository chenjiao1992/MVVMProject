package com.cj.chat.model

import com.cj.chat.im.scim.IMsgContentType
import com.cj.chat.model.interfaces.IMessageContent
import com.cj.library.extension.JsonUtil

/**
 *  Create by chenjiao at  2021/11/26 15:28
 *  描述：
 */
class TextMessage : IMessageContent {
    override fun getContentType() = IMsgContentType.TEXT
    var content: String = ""
    var atGroupMemberList: List<Member>? = null
    var isRevoke = false
    var isSyncRevoke = false

    constructor()
    constructor(content: String?) {
        this.content = content ?: ""
    }

    constructor(content: String?, members: List<Member>?) {
        this.content = content ?: ""
        this.atGroupMemberList = members
    }

    companion object {
        fun create(jsonStr: String?) = JsonUtil.toObject(jsonStr, TextMessage::class.java) ?: TextMessage()

        fun createFromContent(content: String?) = TextMessage(content ?: "")
    }

}