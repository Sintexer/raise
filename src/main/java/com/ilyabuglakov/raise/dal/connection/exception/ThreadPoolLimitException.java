package com.ilyabuglakov.raise.dal.connection.exception;

public class ThreadPoolLimitException extends RuntimeException {
    public ThreadPoolLimitException() {
        super();
    }

    public ThreadPoolLimitException(String message) {
        super(message);
    }

    public ThreadPoolLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThreadPoolLimitException(Throwable cause) {
        super(cause);
    }
}
