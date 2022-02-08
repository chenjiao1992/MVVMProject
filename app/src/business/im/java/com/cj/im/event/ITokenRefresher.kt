package com.cj.im.event

import com.cj.im.config.IMConfig

/**
 *  Create by chenjiao at  2022/2/7 14:40
 *  描述：
 */
interface ITokenRefresher {
    fun refreshTokenSync(config: IMConfig, accountId: Long): String?
}