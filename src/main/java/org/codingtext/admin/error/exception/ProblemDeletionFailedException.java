package org.codingtext.admin.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ProblemDeletionFailedException extends RuntimeException {

    private final HttpStatus status;

    public ProblemDeletionFailedException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
