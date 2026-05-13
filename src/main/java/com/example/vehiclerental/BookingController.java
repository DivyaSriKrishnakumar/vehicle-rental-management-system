package com.example.vehiclerental;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/bookings") 
public class BookingController {
	
		
	@Autowired
	private BookingService bookingService;
	
	
	@PostMapping
	public ResponseEntity<BookingResponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request){
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(bookingService.createBooking(request));
		
	}
	
	@PatchMapping("/{id}/cancel")
	public ResponseEntity<BookingResponseDTO> cancelBooking(@PathVariable Long id) {
	        
	   return ResponseEntity.ok(bookingService.cancelBooking(id));
	}
	
	@GetMapping("/my")
	public ResponseEntity<List<BookingResponseDTO>> getMyBookings() {

	    return ResponseEntity.ok(
	            bookingService.getMyBookings()
	    );
	}
	
	@GetMapping
	public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {

	    return ResponseEntity.ok(
	            bookingService.getAllBookings()
	    );
	}
}
