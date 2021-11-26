package com.cj.chat.im.scim

import android.os.HandlerThread
import android.os.Looper
import android.util.ArrayMap
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 *  Create by chenjiao at  2021/11/25 19:53
 *  描述：
 */
object ChatConstant {
    /**
     * 连接超时时间 35s
     */
    val CONNECT_TIME_OUT = TimeUnit.SECONDS.toMillis((35)) //35S转换为毫秒

    /**
     * 读写数据超时时间15s
     */
    const val SOCKET_WRITE_TIMEOUT = 15L
    private val MSG_SEQ_ID = AtomicInteger(0) //int数自增,在多线程下可以保证线程安全
    const val HEAD_SIZE: Short = 16
    const val VERSION: Short = 0

    /**
     * 发送消息成功后,30s未收到反馈,则认为发送失败
     */
    val TIME_SEND_MSG_TIMEOUT = TimeUnit.SECONDS.toMillis((30))

    /**
     * 每隔4.8分钟一次心跳
     */
    val TIME_HEAT_BEAT_INTERVAL = TimeUnit.MINUTES.toMillis(4) + TimeUnit.SECONDS.toMillis(48)

    fun getSeqId(): Int = MSG_SEQ_ID.incrementAndGet()

    const val INVALIDATE_RESULT = -1

    //todo 多次获取looper是否会一直new HandlerThread
    val LOOPER: Looper = run {
        val handlerThread = HandlerThread("Chat_Looper")
        handlerThread.start()
        handlerThread.looper
    }
}

enum class IMsgContentType(type: Int) {
    UNKOWN(-1),
    TEXT(0),
    IMAGE(1),

    //    MAGIC_PIC(2),
    //    RED_PACK(3),
    //    GIFT(4),
    //    SHARE(5),
    //    SYSTEM(6),
    //    DESCRIPTION(7),
    //    REVOKE(8),
    VOICE(9)
}

enum class INotifyContentType(type: Int) {
    UNKOWN(-1),
    STATUS(11001)

}

enum class IMsgChatType(type: Int) {
    PRIVATE(0),
    GROUP(1),
    CHATROOM(2),
    DISCUZ(3),
    ANINYMITY(4)
}

enum class ISpeakType(type: Int) {
    SELF(0), //本人说
    MEMBER(1), //皮说
    DESCRIPTION(2), //剧情
    SYSTEM(3), //系统
    BACKGROUND(4), //后台
    UNKNOWN(-1)
}

enum class IMsgDirect(type: Int) {
    SEND(0),
    RECEIVE(1),
    UNKNOWN(-1)
}

enum class ICmdMsgType(type: Int) {
    GROUP_JOIN(1), //入群申请
    GROUP_QUIT(2), //退群
    MSG_NOTICE(3), //通知消息
    UNKNOWN(-1);

    companion object {
        fun convert2CmdType(action: String): ICmdMsgType = when (action) {
            "group_join" -> GROUP_JOIN
            "group_quit" -> GROUP_QUIT
            "MSG_NOTICE" -> MSG_NOTICE
            else -> UNKNOWN
        }

    }
}

enum class Operation(val code: Int) {
    OP_HEARTBEAT(2), //每隔4.8分钟一次心跳,BODY不用传
    OP_HEARTBEAT_REPLY(3), //无body
    OP_SEND_MSG(4),
    OP_SEND_MSG_REPLY(5),
    OP_SYNC_MSG(6),
    OP_SYNC_MSG_REPLY(7),

    OP_PUSH_CHAT(8), //服务器推送的消息
    OP_PUSH_MSG(9), //服务器推送的MSG

    OP_SYNC_CHAT(10); //请求同步聊天消息

    companion object {
        private val map = values().associateByTo(ArrayMap(), { it.code }) //associateByTo 根据code为key转map)
        fun fromInt(type: Int): Operation? = map[type]
    }
}

class ReconnectInterval(var times: Int = 1) {
    val duration: StringBuilder = StringBuilder()
    var startConnectTime = 0L

    //连接10次以上都没有成功，则每隔5分钟尝试一次
    //3秒
    //10秒
    val delay: Long
        get() {
            duration.append("time:$times duration=${System.currentTimeMillis() - startConnectTime}")

            return when (++times) {
                1 -> 0L
                in 2..5 -> TimeUnit.SECONDS.toSeconds(3)
                in 6..10 -> TimeUnit.SECONDS.toSeconds(10)
                else -> TimeUnit.MINUTES.toSeconds(5)
            }
        }

    fun reset() {
        times = 0
        startConnectTime = 0
        duration.clear()
    }
}

/**
 * 链接已断开的异常
 */
class DisConException : Exception("It IS disconnect")

/**
 * 数据解析异常
 */
class DataParseException(msg: String) : RuntimeException(msg)

/**
 * 获取聊天服务器地址异常
 */
class GetImServerException(msg: String) : RuntimeException(msg)
