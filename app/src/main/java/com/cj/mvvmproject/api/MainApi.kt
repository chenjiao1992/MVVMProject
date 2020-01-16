package com.cj.mvvmproject.api

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 *  Create by chenjiao at 2020/1/15 0015
 *  描述：
 */
interface MainApi{
    @GET("api/china")
    fun getProvices(@Query("host") baseUrl:String): Observable<ArrayList<Province>>
}