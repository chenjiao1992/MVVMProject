package com.cj.chat.model

import com.cj.chat.im.scim.IMsgContentType
import com.cj.chat.model.interfaces.IMessageContent

class VoiceMessage : IMessageContent {
    var duration = 0
    var url: String? = null

    constructor()
    constructor(duration: Int, url: String) {
        this.duration = duration
        this.url = url
    }

    override fun getContentType() = IMsgContentType.VOICE
}
