package com.cj.sdknet


/**
 *  Create by chenjiao at 2019/4/11 0011
 *  描述：SDKNet必须参数配置, * SdkNet配置参数太多，新建一个类封装起来
 */
class SdkNetConfigParam {
    /**
     * 用于请求Header
     */
    var userAgent: String? = null

    /**
     * 渠道id
     */
    var channeId: String? = null

    /**
     * AId，用于配置请求鉴权参数
     *
     * 必须参数
     */
    var aId: String? = null

    /**
     * 服务器类型，用于配置请求鉴权参数
     *
     * 必须参数
     */
    var serverType: Int = 0

    /**
     * 运行时参数获取回调
     *
     * 必须参数
     */
    var dataProvider: RuntimeDataProvider? = null

    /**
     * 网络连接超时，默认10_000,单位毫秒
     *
     * 取值范围：1 和 {@link Integer#MAX_VALUE}
     *
     * 非必须参数
     */
    var connectTimeOut: Long = 10_100

    /**
     * 网络读超时，默认10_000,单位毫秒
     *
     * 取值范围：1 和 {@link Integer#MAX_VALUE}
     *
     * 非必须参数
     */
    var readTimeOut: Long = 10_100

    /**
     * 网络写超时，默认10_000,单位毫秒
     *
     * 取值范围：1 和 {@link Integer#MAX_VALUE}
     *
     * 非必须参数
     */
    var writeTimeOut: Long = 10_100
}