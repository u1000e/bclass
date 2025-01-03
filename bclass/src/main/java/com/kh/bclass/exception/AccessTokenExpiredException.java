package com.kh.bclass.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccessTokenExpiredException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AccessTokenExpiredException(String message) {
		super(message);
	}
	

}
