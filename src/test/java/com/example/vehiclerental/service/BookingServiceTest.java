package com.example.vehiclerental.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.vehiclerental.AppUser;
import com.example.vehiclerental.Booking;
import com.example.vehiclerental.BookingRepository;
import com.example.vehiclerental.BookingRequestDTO;
import com.example.vehiclerental.BookingResponseDTO;
import com.example.vehiclerental.BookingServiceImpl;
import com.example.vehiclerental.ResourceNotFoundException;
import com.example.vehiclerental.UserRepository;
import com.example.vehiclerental.Vehicle;
import com.example.vehiclerental.VehicleRepository;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Vehicle vehicle;
    private AppUser user;
    private BookingRequestDTO request;

   
    @BeforeEach
    void setUp() {

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setAvailable(true);

        user = new AppUser();
        user.setId(1L);

        request = new BookingRequestDTO();
        request.setVehicleId(1L);
        request.setCustomerId(1L);
    }

    @Test
    void createBooking_ShouldCreateBookingSuccessfully() {
    	
    	Authentication authentication = mock(Authentication.class);

        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn("test@gmail.com");

        SecurityContextHolder.setContext(securityContext);

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookingResponseDTO booking = bookingService.createBooking(request);

        assertNotNull(booking);

        verify(vehicleRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void createBooking_ShouldThrowException_WhenVehicleNotFound() {
    	
    	Authentication authentication = mock(Authentication.class);

        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn("test@gmail.com");

        SecurityContextHolder.setContext(securityContext);

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(request));

        verify(vehicleRepository, times(1)).findById(1L);
        verify(bookingRepository, never()).save(any());
    }
}
