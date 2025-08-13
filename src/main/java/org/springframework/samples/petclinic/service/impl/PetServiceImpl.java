package org.springframework.samples.petclinic.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.exceptions.PetNotFoundException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

	private final PetRepository petRepository;

	public List<Pet> findAll() {
		return petRepository.findAll();
	}

	public Optional<Pet> findById(Integer id) {
		return Optional.ofNullable(
				petRepository.findById(id).orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + id)));
	}

	public void deleteById(Integer id) {
		if (!petRepository.existsById(id)) {
			throw new PetNotFoundException("Pet not found with ID: " + id);
		}
		petRepository.deleteById(id);
	}

	public Pet save(Pet pet) {
		return petRepository.save(pet);
	}

}
