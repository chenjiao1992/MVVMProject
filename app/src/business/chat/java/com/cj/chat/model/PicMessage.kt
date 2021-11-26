package com.cj.chat.model

import com.cj.chat.im.scim.IMsgContentType
import com.cj.chat.model.interfaces.IMessageContent
import com.cj.library.extension.JsonUtil
import java.io.InputStream

class PicMessage : IMessageContent {
    override fun getContentType() = IMsgContentType.IMAGE
    var localUrl: String = ""
    var remoteUrl = ""
    var thumbnaiUrl = ""
    var width = 0
    var height = 0
    var type = 0
    var isExpression = false

    companion object {
        fun create(jsonStr: String?): PicMessage = JsonUtil.toObject(jsonStr, PicMessage::class.java) ?: PicMessage()
        fun create(input: InputStream): PicMessage = JsonUtil.toObject(input, PicMessage::class.java) ?: PicMessage()
    }
}
