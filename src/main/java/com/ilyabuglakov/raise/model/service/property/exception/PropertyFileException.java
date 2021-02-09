package com.ilyabuglakov.raise.model.service.property.exception;

public class PropertyFileException extends Exception {
    public PropertyFileException() {
        super();
    }

    public PropertyFileException(String message) {
        super(message);
    }

    public PropertyFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyFileException(Throwable cause) {
        super(cause);
    }
}
