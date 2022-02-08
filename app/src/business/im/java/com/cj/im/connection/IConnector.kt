package com.cj.im.connection

import com.cj.im.config.IMConfig
import com.cj.im.model.bean.Server
import java.net.Socket

/**
 *  Create by chenjiao at  2022/2/7 14:19
 *  描述：IM服务器连接接口
 */
interface IConnector {
    /**
     * 测试IM服务器连接速度,返回一个最可靠的IM服务器IP和端口
     */
    fun pingServerSync(config:IMConfig,servers:List<Server>):Server

    /**
     * 连接目标IM服务器,打开一个长连接
     */
    fun connectServerSync(config: IMConfig,server:Server):Socket
}