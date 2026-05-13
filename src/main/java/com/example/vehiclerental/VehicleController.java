package com.example.vehiclerental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;

	//Constructor injection (DI)
	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}
	
	@GetMapping("/vehicles/{id}")
	public ResponseEntity<Vehicle> getVehicle(@PathVariable Long id){
		return ResponseEntity.ok(vehicleService.getVehicles(id));
	}
	
	@PostMapping("/vehicles")
	public ResponseEntity<VehicleResponseDTO> addVehicle(@Valid @RequestBody VehicleRequestDTO requestDTO)
	{
		return ResponseEntity.ok(
	            vehicleService.addVehicle(requestDTO));
	}
	
	@DeleteMapping("/vehicles/{id}")
	public ResponseEntity<Void> deleteVehicle(@PathVariable Long id){
		 vehicleService.deleteVehicle(id);
		 return ResponseEntity.noContent().build();
	}
	@PutMapping("/vehicles/{id}")
	public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @Valid @RequestBody Vehicle vehicle){
		Vehicle updateVehicle = vehicleService.updateVehicle(id, vehicle);
		return ResponseEntity.ok(updateVehicle);
		
	}
	@GetMapping("/vehicles")
	public ResponseEntity<Page<Vehicle>> getAllVehicles(

	        @RequestParam(defaultValue = "0") int page,

	        @RequestParam(defaultValue = "5") int size
	) {

	    return ResponseEntity.ok(
	            vehicleService.getAllVehicles(page, size)
	    );
	}
}