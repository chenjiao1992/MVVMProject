package com.cj.chat.model

import com.cj.chat.im.scim.IMsgContentType
import com.cj.chat.model.interfaces.IMessageContent

class UnknownMessage : IMessageContent {
    var type = IMsgContentType.UNKOWN
    var content: String = ""
    var defaultContent = ""

    constructor()
    constructor(type: IMsgContentType, content: String?, defaultContent: String?) {
        this.content = content ?: ""
        this.type = type
        this.defaultContent = defaultContent ?: ""
    }

    override fun getContentType() = IMsgContentType.UNKOWN
}
