package com.ilyabuglakov.raise.model.service.property.exception;

public class PropertyCantInitException extends RuntimeException {
    public PropertyCantInitException() {
        super();
    }

    public PropertyCantInitException(String message) {
        super(message);
    }

    public PropertyCantInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyCantInitException(Throwable cause) {
        super(cause);
    }
}
