package com.cj.sdknet

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：
 */
object NetConstants {
    /**
     * 用于被替换的接口Host
     */
    val mock_host = "https://example.com"
    /**
     * 加密头部名称 todo 需要与服务端约定好的字段
     */
    val NAME_HEADER_ENCRYPT = "encrypt_url"
    /**
     * token参数名
     */
    val name_token = "token"
    /**
     * host参数名
     */
    val name_host = "host"
    /**
     * 重试参数名
     */
    val name_retry = "retryTimes"
    /**
     * 从外界传入的time名称
     */
    val name_time = "name_time"
    /**
     * 无需解析返回json
     */
     val NO_CODE = "no_code"
    /**
     * auth 头部位置
     */
    val name_auth_position = "auth_position"
    /**
     * auth位置放在url后面传递给服务端
     */
    val auth_position_url = "auth_position_url"
    /**
     * auth位置放在头部
     */
    val auth_position_header = "auth_position_header"
    /**
     * auth位置放在post表单
     */
    val auth_position_form = "auth_position_form"
    /**
     * 无需鉴权
     */
    val auth_position_no_auth = "auth_position_no_auth"
    /**
     * 列表使用的offset的请求key
     */
    val KEY_OFFSET = "offset"
    /**
     * 列表使用的limit的key
     */
    val KEY_LIMIT = "limit"
}