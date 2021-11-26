package com.cj.chat.model.interfaces

import com.cj.chat.im.scim.IMsgContentType
import com.cj.chat.model.PicMessage
import com.cj.chat.model.TextMessage
import com.cj.chat.model.UnknownMessage
import com.cj.library.extension.JsonUtil

interface IMessageContent {
    fun getContentType(): IMsgContentType

    companion object {
        fun createMsgContent(type: IMsgContentType, content: String?, defaultContent: String? = null): IMessageContent? {
            val msg = when (type) {
                IMsgContentType.UNKOWN -> createMsg<UnknownMessage>(content ?: "")
                IMsgContentType.TEXT -> TextMessage.createFromContent(content)
                IMsgContentType.IMAGE -> createMsg<PicMessage>(content ?: "")
                IMsgContentType.VOICE -> createMsg<PicMessage>(content ?: "")
            }
            return msg ?: UnknownMessage(type, content, if (defaultContent.isNullOrEmpty()) "低版本不支持该类型消息 请升级最新版本体验哦" else defaultContent)
        }

    }
}

inline fun <reified T : IMessageContent> createMsg(input: String): T? {
    return JsonUtil.toObject(input, T::class.java)
}
