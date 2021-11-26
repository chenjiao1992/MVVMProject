package com.cj.chat

import com.cj.chat.model.Message
import com.cj.chat.model.NotifyMsg
import com.cj.chat.model.SendMsgOption

/**
 *  Create by chenjiao at  2021/11/26 15:13
 *  描述：
 */
interface IChatClient {
    /**
     * 发送消息
     */
    fun sendMessage(message: Message, option: SendMsgOption)

    /**
     * 顺序发送消息
     * pair 可以同时存储两个变量 ,Triple可以同时存储三个变量
     */
    fun sendMessageSequence(list: List<Pair<Message, SendMsgOption>>)

    /**
     * 发送通知消息
     */
    fun sendNotify(notifyMsg: NotifyMsg)
}