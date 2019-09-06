package com.example.webflux.demo.exception;

/**
 * @ClassName ServiceException
 * @Description: ServiceException
 * @Author zhangjiashuai
 * @Date 2019/8/28
 **/
public class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
