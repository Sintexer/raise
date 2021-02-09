package com.ilyabuglakov.raise.dal.dao.exception;

import com.ilyabuglakov.raise.dal.exception.PersistentException;

public class DaoOperationException extends PersistentException {
    public DaoOperationException() {
        super();
    }

    public DaoOperationException(String message) {
        super(message);
    }

    public DaoOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoOperationException(Throwable cause) {
        super(cause);
    }
}
