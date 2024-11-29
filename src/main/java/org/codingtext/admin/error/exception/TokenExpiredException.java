package org.codingtext.admin.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class TokenExpiredException extends RuntimeException {
    private final HttpStatus httpStatus;

    public TokenExpiredException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}