package org.springframework.samples.petclinic.owner;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetAttributesService {

	private final PetAttributesRepository repository;

	public PetAttributesService(PetAttributesRepository repository) {
		this.repository = repository;
	}

	public PetAttributes save(PetAttributes attributes) {
		return repository.save(attributes);
	}

	public Optional<PetAttributes> findById(int id) {
		return repository.findById(id);
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}
}
