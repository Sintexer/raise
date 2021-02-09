package com.ilyabuglakov.raise.config.exception;

public class PoolConfigurationException extends Exception {
    public PoolConfigurationException() {
        super();
    }

    public PoolConfigurationException(String message) {
        super(message);
    }

    public PoolConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PoolConfigurationException(Throwable cause) {
        super(cause);
    }
}
