package com.example.vehiclerental;

public class BookingAlreadyCancelledException extends RuntimeException{
	
	//object version control during serialization/deserialization
	private static final long serialVersionUID = 1L;

	public BookingAlreadyCancelledException(String message) {
		super(message);
	}

}
