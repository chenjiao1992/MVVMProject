package com.cj.im.connection

import com.cj.im.config.IMConfig
import com.cj.im.model.LoginReply

/**
 *  Create by chenjiao at  2022/2/7 14:23
 *  描述：IM服务器登录接口
 */
interface ILogin {
    /**
     * 登录验证
     * 向IM服务器发送账号登录请求,接受返回数据,完成登录验证过程
     *  @param config IM SDK配置参数
     * @return 账号在IM服务器中最新的accountId记录
     */
    fun loginSync(config: IMConfig): LoginReply
}