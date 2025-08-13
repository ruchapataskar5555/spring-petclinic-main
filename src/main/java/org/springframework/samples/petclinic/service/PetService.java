package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetService {

	public List<Pet> findAll();

	public Optional<Pet> findById(Integer id);

	public void deleteById(Integer id);

	public Pet save(Pet pet);

}
