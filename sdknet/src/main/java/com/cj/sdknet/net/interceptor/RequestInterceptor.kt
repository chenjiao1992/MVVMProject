package com.cj.sdknet.net.interceptor

import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.cj.sdknet.Header
import com.cj.sdknet.NetConstants
import com.cj.sdknet.utils.NumberUtil
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：
 */
class RequestInterceptor(userAgent: String, context: Context) : Interceptor {
    private var mContext = context
    private var mUserAgent = userAgent
    override fun intercept(chain: Interceptor.Chain): Response {
        //给原始url增加header
        val originRequest = setUpEncryptUrl(chain)
        //获取重试次数
        val retryTimes = getRetryTimes(originRequest)
        //获取鉴权用的time参数,-1代表未配置
        val time = getEncryptTime(originRequest)
        //获取鉴权结果放置位置，默认通过Http Header将结果传至服务端
        val authPosition = getAuthPosition(originRequest)
        //重新组装url
        val newUrl = setUpHttpUrl(originRequest)
        //通过上一步获取的URL组装Request对象
        var modifiedRequest = originRequest.newBuilder().url(newUrl).build()
        val headers = generateCommonHeaders(mUserAgent)
        //需要鉴权
        if (!TextUtils.equals(NetConstants.auth_position_no_auth, authPosition)) {
            val realTime = if (time.toInt() == -1) System.currentTimeMillis() else time
            //获取鉴权结果
            val auth = getAuth(modifiedRequest, realTime.toString())
            //根据配置，设置鉴权放置位置
            if (TextUtils.equals(NetConstants.auth_position_header, authPosition)) {
                headers.add(Header("time", realTime.toString()))
                headers.add(Header("auth", auth))
            } else if (TextUtils.equals(NetConstants.auth_position_url, authPosition)) {
                val httpUrl = modifiedRequest.url().newBuilder().addQueryParameter("time", realTime.toString()).addQueryParameter("auth", auth)
                    .addQueryParameter("aid", "").build()
                modifiedRequest = modifiedRequest.newBuilder().url(httpUrl).build()
            } else if (TextUtils.equals(NetConstants.auth_position_form, authPosition)) {
                var requestBody = modifiedRequest.body()
                if (requestBody is FormBody) { //暂时只最post表单做处理
                    val formBody = requestBody as FormBody?
                    val builder = FormBody.Builder()
                    for (i in 0 until formBody!!.size()) {
                        builder.add(formBody.name(i), formBody.value(i))
                    }
                    requestBody = builder.add("time", realTime.toString()).add("auth", auth ?: "").build()
                    val url = modifiedRequest.url().newBuilder().addQueryParameter("aid", "").build()
                    modifiedRequest = modifiedRequest.newBuilder().post(requestBody).url(url).build()
                }
            } else {
                throw IllegalArgumentException("name_auth_position value must one of auth_position_header or auth_position_url")
            }
        }
        val newRequest = setUpHeaders(headers, modifiedRequest)
        //放行请求
        var response = chain.proceed(newRequest)
        //执行重试
        var requestTime = 1
        while (!response.isSuccessful && requestTime < retryTimes) {
            response = chain.proceed(newRequest)
            requestTime++
        }
        return response
    }

    /**
     * 设置Http请求头
     * 1，新增必要的头部
     * 2，删除无用头部
     * @param headers
     * @param originalRequest
     * @return
     */
    private fun setUpHeaders(headers: List<Header>, originalRequest: Request): Request {
        val builder = originalRequest.newBuilder()
        for (header in headers) {
            if (header == null || TextUtils.isEmpty(header.key) || TextUtils.isEmpty(header.value)) {
                continue
            }
            builder.addHeader(header.key, header.value)
        }
        builder.removeHeader(NetConstants.NAME_HEADER_ENCRYPT)
        return builder.build()
    }

    private fun getAuth(request: Request, time: String): String? {
        val encryptUrl = request.header(NetConstants.NAME_HEADER_ENCRYPT) //如果没有加密Header则不需要加密
        if (encryptUrl.isNullOrEmpty()) return null
        val getParams = HashMap<String, String>()
        val postParams = HashMap<String, String>()
        //get 参数
        setUpGetParms(request, getParams)
        //post参数
        setUpPostParams(request, postParams)
        return getFinalAuth()
    }

    /**
     * 加密auth的规则,todo 需要与服务端商定
     */
    private fun getFinalAuth(): String? {
        return ""
    }

    private fun setUpPostParams(request: Request, postParams: MutableMap<String, String>) {
        val body = request.body()
        if (body != null) {
            if (body is FormBody) {
                val formBody = body as FormBody?
                val size = formBody!!.size()
                for (i in 0 until size) {
                    postParams[formBody.name(i)] = formBody.value(i)
                }
            } else if (body is MultipartBody) {
                val multipartBody = body as MultipartBody?
                val parts = multipartBody!!.parts()
                if (parts != null && parts.size > 0) {
                    for (p in parts) {
                        val requestBody = p.body()
                        val mediaType = requestBody.contentType()
                        if (isFormData(mediaType)) {
                            val partHeaders = p.headers()
                            val nameValue = partHeaders?.get("Content-Disposition")
                            if (nameValue != null && nameValue.contains("form-data; name=")) {
                                val name = getPartName(nameValue)
                                val value = getPartValue(p)
                                if (name != null && value != null) {
                                    postParams[name] = value
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getPartValue(p: MultipartBody.Part): String? {
        var value: String? = null
        try {
            val buffer = Buffer()
            p.body().writeTo(buffer)
            value = buffer.readUtf8()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return value
    }

    private fun getPartName(nameValue: String): String? {
        val splitStrs = nameValue.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val nameStr = if (splitStrs.size > 1) splitStrs[1] else null
        var name: String? = null
        if (nameStr != null && nameStr.isNotEmpty()) {
            val firstIndex = 1 //第一个引号的位置
            val lastIndex = nameStr.length - 1 //后面一个引号的位置
            if (lastIndex > firstIndex) {
                name = nameStr.substring(firstIndex, lastIndex)
            }
        }
        return name
    }

    private fun isFormData(mediaType: MediaType?): Boolean {
        return mediaType?.subtype() != null && mediaType.subtype().contains("form") || mediaType == null
    }

    private fun setUpGetParms(request: Request, getParams: HashMap<String, String>) {
        val httpUrl = request.url()
        if (httpUrl != null) {
            //获取请求参数
            val parameterNames = httpUrl.queryParameterNames()
            for (name in parameterNames) {
                /**
                 * 去URL中第一个QueryParameter,如果有多个相同name的参数的情况，
                 * 可以使用 queryParameterValues 方法
                 */
                if (!"client_sys".equals(name, ignoreCase = true) && !TextUtils.isEmpty(name)) {  //过滤掉所有不需要加密的参数
                    val value = httpUrl.queryParameter(name)
                    getParams[name] = value.orEmpty()
                }
            }
        }
    }

    /**
     * 添加通用参数 todo 参数跟服务端约定
     */
    private fun generateCommonHeaders(userAgent: String): MutableList<Header> {
        val headers = ArrayList<Header>()
        headers.add(Header("User-Device", ""))
        headers.add(Header("aid", ""))
        headers.add(Header("channel", ""))
        if (userAgent != null) {
            headers.add(Header("User-Agent", userAgent))
        }
        return headers

    }

    private fun setUpHttpUrl(request: Request): HttpUrl {
        val httpUrl = request.url()
        val httpUrlStr = httpUrl.toString()
        val hostPrefix = httpUrl.queryParameter(NetConstants.name_host)
        if (hostPrefix.isNullOrEmpty()) {
            Log.w(TAG, "this url${httpUrlStr}do not config host")
            return httpUrl
        }
        var newUrl = httpUrlStr.replace(NetConstants.mock_host, hostPrefix)
        return HttpUrl.parse(newUrl)!!.newBuilder()
            .addQueryParameter("client_sys", CLIENT_SYS)
            .removeAllQueryParameters(NetConstants.name_host)
            .removeAllQueryParameters(NetConstants.name_retry)
            .removeAllQueryParameters(NetConstants.name_time)
            .removeAllQueryParameters(NetConstants.name_auth_position)
            .build()
    }

    /**
     * 获取auth的位置,如果原始url配置了位置就返回配置的位置,否则就默认auth通过header传递给服务端
     */
    private fun getAuthPosition(originalRequest: Request): String {
        val str = originalRequest.url().queryParameter(NetConstants.name_auth_position)
        return if (str.isNullOrEmpty()) {
            NetConstants.auth_position_header
        } else {
            str.trim()
        }
    }

    /**
     * 获取鉴权用的time参数
     */
    private fun getEncryptTime(originalRequest: Request): Long {
        val str = originalRequest.url().queryParameter(NetConstants.name_time)
        var time = -1
        if (str.isNullOrEmpty()) {
            return time.toLong()
        }

        return NumberUtil.parseIntByCeil(str, -1).toLong()
    }

    /**
     * 通过参数配置的重视次数
     */
    private fun getRetryTimes(originalRequest: Request): Int {
        val retryStr = originalRequest.url().queryParameter(NetConstants.name_retry)
        if (retryStr.isNullOrEmpty()) {
            return 1
        }
        return NumberUtil.parseIntByCeil(retryStr, 1)
    }

    /**
     * 给原始url增加header,参数名是encrypt_url
     */
    private fun setUpEncryptUrl(chain: Interceptor.Chain): Request {
        var originalRequest = chain.request()
        val url = originalRequest.url().toString()
        var encryptHeaderUrl = originalRequest.header(NetConstants.NAME_HEADER_ENCRYPT)
        if (encryptHeaderUrl.isNullOrEmpty()) {
            val firstIndex = NetConstants.mock_host.length + 1
            var lastIndex = url.length
            if (url.contains("?")) {
                lastIndex = url.indexOf("?") + 1
            }
            encryptHeaderUrl = url.substring(firstIndex, lastIndex)
            originalRequest = originalRequest.newBuilder().addHeader(NetConstants.NAME_HEADER_ENCRYPT, encryptHeaderUrl).build()
        }
        return originalRequest
    }

    companion object {
        var TAG = RequestInterceptor::class.java.name
        const val CLIENT_SYS = "android"
    }
}