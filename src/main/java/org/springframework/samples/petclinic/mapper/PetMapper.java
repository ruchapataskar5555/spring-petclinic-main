package org.springframework.samples.petclinic.mapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dtos.PetRequest;
import org.springframework.samples.petclinic.dtos.PetResponse;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetTemperament;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.PetTemperamentService;
import org.springframework.samples.petclinic.service.PetTypeService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetMapper {

	private final ModelMapper modelMapper;

	private final PetTypeService petTypeService;

	private final PetTemperamentService petTemperamentService;

	public Pet toEntity(PetRequest dto) {
		Pet pet = modelMapper.map(dto, Pet.class);
		PetType petType = petTypeService.findPetTypeByName(dto.getType()).orElseGet(() -> {
			PetType newType = new PetType();
			newType.setName(dto.getType());
			return petTypeService.save(newType);
		});
		pet.setType(petType);
		PetTemperament petTemperament = petTemperamentService.findPetTemperament(dto.getTemperament()).orElseGet(() -> {
			PetTemperament newTemp = new PetTemperament();
			newTemp.setTemperament(dto.getTemperament());
			return petTemperamentService.save(newTemp);
		});
		pet.setPetTemperament(petTemperament);
		return pet;
	}

	public PetResponse toDto(Pet pet) {
		PetResponse dto = modelMapper.map(pet, PetResponse.class);
		dto.setType(pet.getType().getName());
		dto.setTemperament(pet.getPetTemperament().getTemperament());
		return dto;
	}

	public void updateEntityFromDto(@Valid PetRequest dto, Pet existingPet) {

		modelMapper.map(dto, existingPet);
		PetType petType = petTypeService.findPetTypeByName(dto.getType()).orElseGet(() -> {
			PetType newType = new PetType();
			newType.setName(dto.getType());
			return petTypeService.save(newType);
		});
		existingPet.setType(petType);
		PetTemperament petTemperament = petTemperamentService.findPetTemperament(dto.getTemperament()).orElseGet(() -> {
			PetTemperament newTemp = new PetTemperament();
			newTemp.setTemperament(dto.getTemperament());
			return petTemperamentService.save(newTemp);
		});
		existingPet.setPetTemperament(petTemperament);
	}

}
