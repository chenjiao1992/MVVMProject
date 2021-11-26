package com.cj.chat.model.interfaces

import com.cj.chat.im.scim.INotifyContentType
import com.cj.chat.model.StatusNotify
import com.cj.chat.model.annotation.NotifyContentType
import com.cj.library.extension.JsonUtil
import java.io.InputStream

interface INotifyContent {
    fun getContentType(): INotifyContentType {
        if (this::class.java.isAnnotationPresent(NotifyContentType::class.java)) {
            val annotation = this::class.java.getAnnotation(NotifyContentType::class.java)
            return annotation?.type ?: INotifyContentType.UNKOWN
        }
        return INotifyContentType.UNKOWN
    }

    fun customHashCode() = -1

    companion object {
        fun create(input: InputStream, contentType: INotifyContentType): INotifyContent? {
            return when (contentType) {
                INotifyContentType.STATUS -> createNotify<StatusNotify>(input)
                else -> null
            }
        }
    }
}

/**
 * inline 与reified搭配,方法写在类之外,调用时表示可以直接整个方法传入
 */
inline fun <reified T : INotifyContent> createNotify(input: InputStream): T? {
    return JsonUtil.toObject(input, T::class.java)
}
