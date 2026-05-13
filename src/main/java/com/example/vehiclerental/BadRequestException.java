package com.example.vehiclerental;

public class BadRequestException extends RuntimeException {
	
	//object version control during serialization/deserialization
	private static final long serialVersionUID = 1L;

	public BadRequestException(String message) {
		super(message);
	}

}
