package com.kh.bclass.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateUserException extends RuntimeException{
    private static final long serialVersionUID = 1L;
	
	public DuplicateUserException(String message) {
		super(message);
	}

	
}
