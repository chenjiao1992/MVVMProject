package com.cj.chat.model.annotation

import com.cj.chat.im.scim.INotifyContentType

/**
 *  Create by chenjiao at  2021/11/26 20:06
 *  描述：
 */
@Target(AnnotationTarget.CLASS)
annotation class NotifyContentType(val type: INotifyContentType) {
}