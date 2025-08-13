package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.PetType;

import java.util.Optional;

public interface PetTypeService {

	public Optional<PetType> findPetTypeByName(String type);

	public PetType save(PetType petType);

}
