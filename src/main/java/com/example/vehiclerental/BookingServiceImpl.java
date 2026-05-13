package com.example.vehiclerental;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

@Service
@Validated
public class BookingServiceImpl implements BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public BookingResponseDTO createBooking(@Valid  BookingRequestDTO request) {
		
		//get login user information
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String username = authentication.getName();
		
		String role = authentication.getAuthorities()
				      .iterator()
				      .next()
				      .getAuthority();
		long customerId;
		
		//USER role -> only book for themselves
		if(role.equals("ROLE_USER")) {
			 AppUser loggedInUser = userRepository.findByUsername(username)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException("Logged-in user not found"));
			 
			 if(request.getCustomerId().longValue() != loggedInUser.getId().longValue()) {

			        throw new BadRequestException(
			                "You can book only for your own account"
			        );
			  }

	            customerId = loggedInUser.getId();
	    }
		
		//ADMIN role -> can book for any user
		else if(role.equals("ROLE_ADMIN")) {
			if(request.getCustomerId() == null) {
				throw new BadRequestException("Customer ID is required");
			}
			if(!userRepository.existsById(request.getCustomerId())) {
				throw new ResourceNotFoundException("Customer not found");
			}
			customerId = request.getCustomerId();
		}
		
		else {
			throw new BadRequestException("Invalid Role");
		}
		
		
		 System.out.println("Vehicle ID: " + request.getVehicleId());
		 System.out.println("Customer ID: " + request.getCustomerId());
		
		// Validate vehicle exists
		Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
		  .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
		 

        // Check availability (maintenance case)
        if (!Boolean.TRUE.equals(vehicle.getAvailable())) {
            throw new BadRequestException("Vehicle is not available (under maintenance)");
        }
		
		//validate startdate , enddate.
		if(request.getStartDate().isAfter(request.getEndDate())) {
			throw new BadRequestException("Startdate must be before enddate");
		}
		
		//  Check booking overlap (core logic)
        boolean conflict = bookingRepository.existsOverlappingBooking(
                request.getVehicleId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (conflict) {
            throw new BadRequestException("Vehicle already booked for selected dates");
        }
		
		//create booking
        Booking booking = new Booking();

        booking.setVehicleId(request.getVehicleId());
        booking.setCustomerId(customerId);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setStatus(BookingStatus.BOOKED);

        Booking savedBooking =
                bookingRepository.save(booking);

        return mapToDTO(savedBooking);
    }
		

	@Override
	public BookingResponseDTO cancelBooking(Long bookingId) {
		
		//get login user information
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
					
		String username = authentication.getName();
					
		String role = authentication.getAuthorities()
					  .iterator()
					  .next()
					  .getAuthority();
		
		 // Find booking
		Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
		
		// USER role -> cancel only own booking
		if(role.equals("ROLE_USER")) {
			AppUser loggedInUser = userRepository.findByUsername(username)
					.orElseThrow(()-> new ResourceNotFoundException("Logged-in user not found"));
			
			//booking user check
			if(!booking.getCustomerId().equals(loggedInUser.getId())) {
				throw new BadRequestException("You can cancel only your own bookings");
			}
			
		}
		//ADMIN role -> cancel any booking
		else if(role.equals("ROLE_ADMIN")) {
			System.out.println("ADMIN access granted for cancellation");
		}
		
		//Invalid Role
		else {
			throw new BadRequestException("Invalid Role");
		}
		
        //checking the flow
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Booking found: " + booking);
        System.out.println("Status: " + booking.getStatus());

        // Check if already cancelled
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("Booking is already cancelled");
        }

        // Update status
        booking.setStatus(BookingStatus.CANCELLED);

        //Save and return
        Booking updatedBooking =
                bookingRepository.save(booking);

        return mapToDTO(updatedBooking);
    }	
	
	@Override
	public List<BookingResponseDTO> getMyBookings() {

	    // Get logged-in user
	    Authentication authentication =
	            SecurityContextHolder.getContext()
	                    .getAuthentication();

	    String username = authentication.getName();

	    // Find logged-in AppUser
	    AppUser loggedInUser =
	            userRepository.findByUsername(username)
	                    .orElseThrow(() ->
	                            new ResourceNotFoundException(
	                                    "User not found"
	                            ));

	    // Return only this user's bookings
	    return bookingRepository
                .findByCustomerId(loggedInUser.getId())
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
	}
	
	@Override
	public List<BookingResponseDTO> getAllBookings() {

		 return bookingRepository.findAll()
	                .stream()
	                .map(this::mapToDTO)
	                .collect(Collectors.toList());
	}
	
	 // ENTITY -> DTO MAPPER
    private BookingResponseDTO mapToDTO(
            Booking booking) {

        BookingResponseDTO dto =
                new BookingResponseDTO();

        dto.setId(booking.getId());
        dto.setVehicleId(booking.getVehicleId());
        dto.setCustomerId(booking.getCustomerId());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setStatus(booking.getStatus());

        return dto;
    }

}