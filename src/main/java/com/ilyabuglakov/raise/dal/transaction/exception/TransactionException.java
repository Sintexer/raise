package com.ilyabuglakov.raise.dal.transaction.exception;

import com.ilyabuglakov.raise.dal.exception.PersistentException;

public class TransactionException extends PersistentException {
    public TransactionException() {
        super();
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
