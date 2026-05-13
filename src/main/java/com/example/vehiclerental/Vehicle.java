package com.example.vehiclerental;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="vehicle")
public class Vehicle {
	
	@Id
	@NotNull(message="vehicle Id should be provided")
	private Long id;
    @NotBlank(message="vehicle name is required")
	private String name;
    @NotBlank(message="vehicle type should be provided")
	private String type;
	@NotNull(message ="vehicle available or under service must be provided")
	private Boolean available=true;
	
	//Getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

}
