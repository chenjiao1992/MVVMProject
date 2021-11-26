package com.cj.chat.model

import com.cj.chat.im.scim.INotifyContentType
import com.cj.chat.model.interfaces.INotifyContent

/**
 *  Create by chenjiao at  2021/11/26 20:22
 *  描述：
 */
class StatusNotify : INotifyContent {
    override fun getContentType() = INotifyContentType.STATUS
}