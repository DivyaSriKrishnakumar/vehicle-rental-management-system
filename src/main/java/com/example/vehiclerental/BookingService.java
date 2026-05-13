package com.example.vehiclerental;

import java.util.List;

import jakarta.validation.Valid;

public interface BookingService {
	
	BookingResponseDTO createBooking(@Valid BookingRequestDTO request);
	
	BookingResponseDTO cancelBooking(Long bookingId);
	
	List<BookingResponseDTO> getMyBookings();
	
	List<BookingResponseDTO> getAllBookings();

}
