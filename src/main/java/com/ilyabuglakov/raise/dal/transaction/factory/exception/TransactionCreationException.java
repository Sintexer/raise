package com.ilyabuglakov.raise.dal.transaction.factory.exception;

public class TransactionCreationException extends RuntimeException {
    public TransactionCreationException() {
        super();
    }

    public TransactionCreationException(String message) {
        super(message);
    }

    public TransactionCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionCreationException(Throwable cause) {
        super(cause);
    }
}
