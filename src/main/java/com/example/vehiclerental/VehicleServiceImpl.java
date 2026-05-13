package com.example.vehiclerental;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VehicleServiceImpl implements VehicleService{

	@Autowired	
	private VehicleRepository vehicleRepository;

	@Override
	public VehicleResponseDTO addVehicle(
	        VehicleRequestDTO requestDTO) {

	    // Check duplicate vehicle ID
	    if(vehicleRepository.existsById(requestDTO.getId())) 
	    {
	    	throw new BadRequestException( "Vehicle with ID "+ requestDTO.getId()
	                + " already exists");
	    }

	    // DTO -> Entity mapping
	    Vehicle vehicle = new Vehicle();

	    vehicle.setId(requestDTO.getId());
	    vehicle.setName(requestDTO.getName());
	    vehicle.setType(requestDTO.getType());
	    vehicle.setAvailable(requestDTO.getAvailable());

	    // Save entity
	    Vehicle savedVehicle = vehicleRepository.save(vehicle);

	    // Entity -> ResponseDTO mapping
	    VehicleResponseDTO responseDTO = new VehicleResponseDTO();

	    responseDTO.setId(savedVehicle.getId());
	    responseDTO.setName(savedVehicle.getName());
	    responseDTO.setType(savedVehicle.getType());
	    responseDTO.setAvailable(savedVehicle.getAvailable());

	    return responseDTO;
	}
	
	@Override
    public void deleteVehicle(Long id) {
    	if(!vehicleRepository.existsById(id)) {
    		throw new ResourceNotFoundException("Vehicle not found of id:" +id);
    	}
    	vehicleRepository.deleteById(id);
    }

	@Override
	public Vehicle getVehicles(Long id) {
		return vehicleRepository.findById(id)
				.orElseThrow(() -> 
				        new ResourceNotFoundException("Vehicle not found of id:" +id)
			);
	}

	@Override 
	public Vehicle updateVehicle(Long id, Vehicle vehicle) { 
		
		Vehicle existingVehicle = vehicleRepository.findById(id) 
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + id));
	  
	  // Update fields (DO NOT replace object directly)
	  existingVehicle.setName(vehicle.getName());
	  existingVehicle.setType(vehicle.getType());
	  existingVehicle.setAvailable(vehicle.getAvailable());
	  
	  return vehicleRepository.save(existingVehicle); 
	}

	@Override
	public Page<Vehicle> getAllVehicles(int page, int size) {
		
		Pageable pageable = PageRequest.of(page, size);

	    return vehicleRepository.findAll(pageable);
	}	 
}