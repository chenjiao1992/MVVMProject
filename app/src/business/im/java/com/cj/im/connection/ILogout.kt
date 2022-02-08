package com.cj.im.connection

import com.cj.im.config.IMConfig

/**
 *  Create by chenjiao at  2022/2/7 14:27
 *  描述：
 */
interface ILogout {
    fun logoutSync(config: IMConfig)
}