package com.cj.im.config

import com.cj.im.callback.ISimpleDispatcher
import com.cj.im.client.IMClient
import com.cj.im.connection.IConnector
import com.cj.im.connection.IGatewayFetcher
import com.cj.im.connection.ILogin
import com.cj.im.connection.ILogout
import com.cj.im.core.io.ISocketIO
import com.cj.im.event.ILoginExceptionHandler
import com.cj.im.event.ITokenRefresher
import com.cj.im.heartbeat.IHeartbeat
import com.cj.im.model.bean.IMemberTarget
import com.cj.im.transmission.IHttpTransmission
import com.cj.im.transmission.IPacketTransmission
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.HashSet

/**
 *  Create by chenjiao at  2022/2/7 10:06
 *  描述：
 */
class IMConfig private constructor() {
    var status = IMClient.STATUS_IDLE
        internal set(value) { //internal修复类的方法,表示只能在本module中调用
            if (field == value) return
            field = value
            statusDispatcher.dispatch(value)

        }
    var accountId = 0L
        internal set
    var imToken: String? = null
        internal set
    var watchingRoomId = 0L
        internal set
    var peekingRoomId = 0L
        internal set
    var peekingShouldUnsubscribe = false
        internal set
    var myMember: IMemberTarget? = null
        internal set

    /**
     * Set 有序不重复
     * map:无序不重复
     * array:有序可重复
     */
    val subscribeRoomIds: MutableSet<Long> = Collections.synchronizedSet(HashSet<Long>())
    val roomSynced = Collections.synchronizedSet(HashSet<Long>())

    /**
     * AtomicInteger原子操作类,保证多线程下自增自减是线程安全的
     */
    private val mSequenceId = AtomicInteger(0)
    var sequenceId: Int
        get() = mSequenceId.incrementAndGet()
        internal set(value) {
            mSequenceId.set(value)
        }
    lateinit var socketIo: ISocketIO
        private set
    lateinit var gatewayFetcher: IGatewayFetcher
        private set
    lateinit var connector: IConnector
        private set
    lateinit var login: ILogin
        private set
    lateinit var logout: ILogout
        private set
    lateinit var loginExceptionHandler: ILoginExceptionHandler
    lateinit var tokenRefresher: ITokenRefresher
        private set
    lateinit var heartbeat: IHeartbeat
        private set
    lateinit var httpTransmission: IHttpTransmission
        private set
    lateinit var packetTransmission: IPacketTransmission
    lateinit var statusDispatcher: ISimpleDispatcher<Int>
        private set

}