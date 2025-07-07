package org.example.file;

public class ValidationException extends Exception {
    private final ErrorType code;

    public ValidationException(ErrorType code, String message) {
        super(message);
        this.code = code;
    }

    public ErrorType getCode() {
        return code;
    }
}