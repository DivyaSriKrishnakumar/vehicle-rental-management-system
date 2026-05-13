package com.example.vehiclerental;

public class ResourceNotFoundException extends RuntimeException {
	
	//object version control during serialization/deserialization
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(String message) {
		super(message);
	}

}
