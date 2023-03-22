package com.realworld.backend.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
	private static final long serialVersionUID = 7157853757076932943L;

	private final Error error;
	
	public AppException(Error error) {
		super(error.getMessage());
		this.error = error;
	}
}
