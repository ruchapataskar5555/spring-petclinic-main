package org.springframework.samples.petclinic.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetRequest {

	@NotBlank(message = "Pet name is required")
	private String name;

	@NotNull(message = "Birth date is required")
	@Past(message = "Birth date must be in the past")
	private LocalDate birthDate;

	@NotNull(message="Pet Type is required")
	private String type;

	@Size(max = 50, message = "Temperament must not exceed 50 characters")
	private String temperament;

	@NotNull(message = "Length is required")
	private Double length;

	@NotNull(message = "weight is required")
	private Double weight;

}
