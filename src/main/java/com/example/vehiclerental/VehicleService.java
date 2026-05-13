package com.example.vehiclerental;


import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;

@Service
public interface VehicleService {
	
	//List<Vehicle> getAllVehicles();
	Page<Vehicle> getAllVehicles(int page, int size);
	VehicleResponseDTO addVehicle(@Valid VehicleRequestDTO requestDTO);
	void deleteVehicle(Long id);
	Vehicle getVehicles(Long id);
	Vehicle updateVehicle(Long id, Vehicle vehicle);

}
