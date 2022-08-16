package com.autumn.exception;

/**
 * 身份证异常
 */
public class IdCardException extends Exception{

    public IdCardException() {
    }

    public IdCardException(String message) {
        super(message);
    }

    public IdCardException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdCardException(Throwable cause) {
        super(cause);
    }

    public IdCardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
