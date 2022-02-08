package com.cj.im.connection

import com.cj.im.config.IMConfig
import com.cj.im.model.bean.Server

/**
 *  Create by chenjiao at  2022/2/7 14:12
 *  描述：IM服务器获取接口
 */
interface IGatewayFetcher {
    /**
     * 通过业务接口,获取IM服务器信息(IP,端口等)列表
     */
    fun fetchServersSync(config: IMConfig): List<Server>
}