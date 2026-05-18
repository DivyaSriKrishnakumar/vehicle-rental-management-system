package com.example.vehiclerental.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        request.setStartDate(
                LocalDate.now().plusDays(1));

        request.setEndDate(
                LocalDate.now().plusDays(3));
    }

    @Test
    void createBooking_ShouldCreateBookingSuccessfully() {

        Authentication authentication =
                mock(Authentication.class);

        List<GrantedAuthority> authorities =
                new ArrayList<>();

        authorities.add(
                new SimpleGrantedAuthority("ROLE_ADMIN"));

        when(authentication.getAuthorities())
                .thenAnswer(invocation -> authorities);

        when(authentication.getName())
                .thenReturn("admin");

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.of(vehicle));

        when(bookingRepository.existsOverlappingBooking(
                anyLong(),
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(false);

        when(bookingRepository.save(any(Booking.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        BookingResponseDTO booking =
                bookingService.createBooking(request);

        assertNotNull(booking);

        verify(vehicleRepository, times(1))
                .findById(1L);

        verify(bookingRepository, times(1))
                .save(any(Booking.class));
    }

    @Test
    void createBooking_ShouldThrowException_WhenVehicleNotFound() {

        Authentication authentication =
                mock(Authentication.class);

        List<GrantedAuthority> authorities =
                new ArrayList<>();

        authorities.add(
                new SimpleGrantedAuthority("ROLE_ADMIN"));

        when(authentication.getAuthorities())
                .thenAnswer(invocation -> authorities);

        when(authentication.getName())
                .thenReturn("admin");

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        when(userRepository.existsById(1L))
                .thenReturn(true);

        when(vehicleRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(request));

        verify(vehicleRepository, times(1))
                .findById(1L);

        verify(bookingRepository, never())
                .save(any());
    }
}