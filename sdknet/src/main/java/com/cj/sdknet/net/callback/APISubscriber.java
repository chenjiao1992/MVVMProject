package com.cj.sdknet.net.callback;


import android.view.WindowManager;

import com.cj.sdknet.net.exceptions.LocalDataException;
import com.cj.sdknet.net.exceptions.ServerException;
import com.cj.sdknet.net.exceptions.TransformException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 描述：网络请求接口回调，主要对网络异常做统一响应
 * 客户端网络请求callback 父类。如果token过期，会换客户端的token
 *
 *
 */

public abstract class APISubscriber<T> extends Subscriber<T> {

    public static final int UNKNOWN = 1000;

    public static final int SOCKET_TIME_OUT_ERROR = 6002;//socket超时

    public static final int UNKNOWN_HOST_ERROR = 6003;//无法解析host

    public static final int SOCKET_CONNECT_ERROR = 6004;//网络连接错误，一般是断网问题或者IP对应的服务端进程出现错误

    public static final int SSL_HANDSHAKE_ERROR = 6005;//https 握手出现错误，常见证书出现问题

    public static final int JSON_PARSE_ERROR = 11000;//JSON解析错误

    public static final int NULL_POINTER_ERROR = 11001;//应用层异常-空指针异常

    public static final int INDEX_OUT_OF_BOUNDS_ERROR = 11002;//应用层异常-数组越界异常

    public static final int BAD_TOKEN_ERROR = 11003;//应用层异常-BadTokenException

    public static final int IO_ERROR = 11004;//应用层异常-IO异常

    public static final int LOCAL_FOLLOW_ERROR = 11005;//应用层异常-本地关注异常

    @Override
    public  void onError(Throwable e) {
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        if (e instanceof HttpException) { //HTTP错误
            HttpException httpException = (HttpException) e;
            onError(httpException.code(), httpException.code() + " 网络异常", e);
            writeLog(String.valueOf(httpException.code()), httpException.message(), httpException);
        } else if (e instanceof ServerException) {//服务器返回的错误
            ServerException serverException = (ServerException) e;
            onError(serverException.code, serverException.msg, e);
            String errorCode = String.valueOf(serverException.code);
            checkToken(errorCode);
        } else if (e instanceof SocketTimeoutException){
            onError(SOCKET_TIME_OUT_ERROR,SOCKET_TIME_OUT_ERROR+"  网络超时异常",e);
        } else if (e instanceof UnknownHostException){
            onError(UNKNOWN_HOST_ERROR,UNKNOWN_HOST_ERROR+" 未知主机异常",e);
        } else if (e instanceof ConnectException){
            onError(SOCKET_CONNECT_ERROR,SOCKET_CONNECT_ERROR+" 网络连接异常",e);
        } else if (e instanceof SSLHandshakeException){
            onError(SSL_HANDSHAKE_ERROR,SSL_HANDSHAKE_ERROR+" https异常",e);
        } else if (e instanceof TransformException){
            onError(JSON_PARSE_ERROR, JSON_PARSE_ERROR + "  数据解析异常",e);
        } else if (e instanceof NullPointerException){
            onError(NULL_POINTER_ERROR,NULL_POINTER_ERROR+" 数据处理异常",e);
        } else if (e instanceof IndexOutOfBoundsException){
            onError(INDEX_OUT_OF_BOUNDS_ERROR,INDEX_OUT_OF_BOUNDS_ERROR+" 数据处理异常",e);
        } else if (e instanceof WindowManager.BadTokenException){
            onError(BAD_TOKEN_ERROR,BAD_TOKEN_ERROR+" 数据处理异常",e);
        } else if (e instanceof IOException){
            onError(IO_ERROR,IO_ERROR+" 数据处理异常",e);
        } else if (e instanceof LocalDataException){
            onError(LOCAL_FOLLOW_ERROR,e.getMessage(),e);
        }else {
            onError(UNKNOWN,UNKNOWN + " 未知异常",e);
        }
        e.printStackTrace();
    }

    private void checkToken(String errorCode) {
//        if (TextUtils.equals(errorCode, ErrorCode.API_TOKEN_OVERDUE)
//            || TextUtils.equals(errorCode, ErrorCode.API_TOKEN_ERROR)
//            || TextUtils.equals(errorCode, ErrorCode.API_LUA_TOKEN_ERROR)) {
//            if (isAnchor()) {
//                DyNetworkBusinessManager
//                    .doBusiness(DyNetworkBusinessManager.BUSINESS_ANCHOR_TOKEN_INVALID, DyNetworkBusinessManager.BusinessCallback.ACTION_DEFAULT, (Object) null);
//            } else {
//                DyNetworkBusinessManager
//                    .doBusiness(DyNetworkBusinessManager.BUSINESS_USER_TOKEN_INVALID, DyNetworkBusinessManager.BusinessCallback.ACTION_DEFAULT, (Object) null);
//            }
//        }
    }

    /**
     * 写日志
     *
     * @param code 错误码
     * @param msg 错误信息
     * @param httpException exception
     */
    private void writeLog(String code, String msg, HttpException httpException) {
        String url = "";
        try {
            url = httpException.response().raw().request().url().toString();
        } catch (Exception e) {
            url = "";
        }
        String errorbodystr = "-->" + code;
//        DyNetworkBusinessManager.doBusiness(DyNetworkBusinessManager.BUSINESS_LOG_UPLOAD, DyNetworkBusinessManager.LogUploadCallback.ACTION_CRASH_REPORT,
//            errorbodystr, url, code, msg, httpException);
    }

    /**
     * 是否是主播
     */
    protected boolean isAnchor() {
        return false;
    }

    /**
     * 网路请求之后错误的回调，此时已对异常作出统一的处理
     *
     * @param code 错误码
     * @param message 错误信息
     */
    protected abstract void onError(int code, String message, Throwable t);

    @Override
    public void onCompleted() {
    }
}