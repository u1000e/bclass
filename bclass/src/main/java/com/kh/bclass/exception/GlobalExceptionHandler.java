package com.kh.bclass.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // MethodArgumentNotValidException 핸들러 오버라이드
    @Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        ErrorDetail errorDetails = new ErrorDetail(new Date(), "유효하지 않은 값", errors.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    

	/*
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetail> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        ErrorDetail errorDetails = new ErrorDetail(new Date(), "권한이 존재하지 않습니다.", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
	*/
    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<?> handleAccessTokenExpiredException(AccessTokenExpiredException ex, WebRequest request){
    	ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(), request.getDescription(false));
    	return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> handleNotFoundUserException(UsernameNotFoundException ex, WebRequest request){
		ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateUserException.class)
	public ResponseEntity<?> handleConflictUserException(DuplicateUserException ex, WebRequest request){
		ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}
	/*
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }
    */
/*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    */

}
