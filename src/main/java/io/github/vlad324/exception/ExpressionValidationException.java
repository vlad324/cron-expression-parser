package io.github.vlad324.exception;

public class ExpressionValidationException extends RuntimeException {

    public ExpressionValidationException(String message) {
        super(message);
    }

    public ExpressionValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
