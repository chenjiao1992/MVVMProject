package com.cj.sdknet.net.exceptions;

/**
 * 描述：用于描述 HttpRespons到JavaBean的转换出现的异常
 */

public class TransformException extends RuntimeException {

    public TransformException() {
    }

    public TransformException(String message) {
        super(message);
    }

    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformException(Throwable cause) {
        super(cause);
    }
}
