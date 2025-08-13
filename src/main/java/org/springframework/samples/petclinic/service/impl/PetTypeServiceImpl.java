package org.springframework.samples.petclinic.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetTypeRepository;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetTypeServiceImpl implements PetTypeService {

	private final PetTypeRepository petTypeRepository;

	@Override
	public Optional<PetType> findPetTypeByName(String type) {
		return Optional.ofNullable(petTypeRepository.findByName(type));
	}

	@Override
	public PetType save(PetType petType) {
		return petTypeRepository.save(petType);
	}

}
