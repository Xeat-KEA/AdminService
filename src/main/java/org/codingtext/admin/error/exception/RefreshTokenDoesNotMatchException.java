package org.codingtext.admin.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RefreshTokenDoesNotMatchException extends RuntimeException {

    private final HttpStatus httpStatus;

    public RefreshTokenDoesNotMatchException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
