package com.cj.sdknet.net.exceptions;

/**
 */
public class ServerException extends RuntimeException {

    public int code;

    public String msg;

    public ServerException(String msg){
        super(msg);
        this.msg = msg;
    }

    public ServerException(int code, String msg){
        super(msg);
        this.msg = msg;
        this.code = code;
    }


}