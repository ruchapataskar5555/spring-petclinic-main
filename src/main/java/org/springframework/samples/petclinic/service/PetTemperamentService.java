package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.PetTemperament;
import org.springframework.samples.petclinic.repository.PetTemperamentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface PetTemperamentService {

	public PetTemperament save(PetTemperament petTemperament);

	public Optional<PetTemperament> findById(int id);

	public void deleteById(int id);

	public Optional<PetTemperament> findPetTemperament(String temperament);

}
