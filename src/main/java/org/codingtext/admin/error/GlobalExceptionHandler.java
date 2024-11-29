package org.codingtext.admin.error;

import org.codingtext.admin.error.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAdminDeletionException(PermissionDeniedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(AdminNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidPasswordException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AnnounceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAnnounceNotFoundExceptionException(AnnounceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenDoesNotMatchException.class)
    public ResponseEntity<ErrorResponse> hdRefreshTokenMismatch(RefreshTokenDoesNotMatchException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse,httpStatus);
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> hdTokenExpired(TokenExpiredException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse,httpStatus);
    }

    @ExceptionHandler(TokenTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> hdTypeMismatch(TokenTypeMismatchException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse,httpStatus);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<ErrorResponse> hdUnauthenticatedEx(UnauthenticatedException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse,httpStatus);
    }
}