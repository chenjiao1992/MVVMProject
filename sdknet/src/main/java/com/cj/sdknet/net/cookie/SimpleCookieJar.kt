package com.cj.sdknet.net.cookie

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：
 */
class SimpleCookieJar : CookieJar {
    private final val mAllCookies = ArrayList<Cookie>()
    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
        mAllCookies.addAll(cookies)
    }

    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
        var result = ArrayList<Cookie>()
        mAllCookies.forEach {
            if (it.matches(url)) {
                result.add(it)
            }
        }
        return result
    }

}