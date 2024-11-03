package org.codingtext.admin.error.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String message) {
        super(message);
    }
}