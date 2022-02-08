package com.cj.im.transmission

import com.cj.im.RequestBuilder
import com.cj.im.config.IMConfig

/**
 *  Create by chenjiao at  2022/2/7 15:13
 *  描述：Http请求传输接口
 *  用于向远端服务器发起http请求
 */
interface IHttpTransmission {
    fun sync(config: IMConfig,requestBuilder: RequestBuilder):String
}