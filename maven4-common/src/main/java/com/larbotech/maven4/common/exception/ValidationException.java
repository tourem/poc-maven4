package com.larbotech.maven4.common.exception;

/**
 * Exception personnalisée pour les erreurs de validation
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
