package org.codingtext.admin.error.exception;

import org.springframework.http.HttpStatus;

public class ProblemCreationFailedException extends RuntimeException {
    private final HttpStatus status;

    public ProblemCreationFailedException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
