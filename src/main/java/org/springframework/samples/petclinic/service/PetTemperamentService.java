package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.PetTemperament;
import org.springframework.samples.petclinic.repository.PetTemperamentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PetTemperamentService {

	private final PetTemperamentRepository repository;

	public PetTemperamentService(PetTemperamentRepository repository) {
		this.repository = repository;
	}

	public PetTemperament save(PetTemperament petTemperament) {
		return repository.save(petTemperament);
	}

	public Optional<PetTemperament> findById(int id) {
		return repository.findById(id);
	}

	public void deleteById(int id) {
		repository.deleteById(id);
	}

	public Optional<PetTemperament> findPetTemperament(String temperament){
		return repository.findByTemperament(temperament);
	}


}
