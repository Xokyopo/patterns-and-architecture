package ru.geekbrains.arhitecture.patterns.objectmapper.dao.orm.exception;

public class UncheckedSQLException extends RuntimeException {
    public UncheckedSQLException() {
        super();
    }

    public UncheckedSQLException(String message) {
        super(message);
    }

    public UncheckedSQLException(String message, Throwable cause) {
        super(message, cause);
    }

    public UncheckedSQLException(Throwable cause) {
        super(cause);
    }

    protected UncheckedSQLException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
