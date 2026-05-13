package com.example.vehiclerental;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long>{
	
	//Spring cannot automatically generate this logic, use custom query.
	@Query("""
			SELECT COUNT(b) > 0 FROM Booking b
			WHERE b.vehicleId = :vehicleId
			AND b.startDate <= :endDate
			AND b.endDate   >= :startDate
			""")
	boolean existsOverlappingBooking(
			 @Param("vehicleId") Long vehicleId,
			 @Param("startDate") LocalDate startDate,
			 @Param("endDate") LocalDate endDate
	);
	
	List<Booking> findByCustomerId(Long customerId);
	

}
