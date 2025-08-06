package org.springframework.samples.petclinic.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.samples.petclinic.dtos.petDto.PetRequest;
import org.springframework.samples.petclinic.dtos.petDto.PetResponse;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetTemperament;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PetTemperamentService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PetJsonMapper {

	private final ObjectMapper objectMapper;
	private final PetTemperamentService petTemperamentService;

	public PetJsonMapper( PetTemperamentService petTemperamentService1) {
		this.petTemperamentService = petTemperamentService1;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule()); // for LocalDate
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	}

	public PetResponse toDto(Pet pet) {
		if (pet == null) {
			return null;
		}
		PetResponse petResponse=objectMapper.convertValue(pet, PetResponse.class);
		petResponse.setTemperament(String.valueOf(pet.getPetTemperament().getTemperament()));
		return objectMapper.convertValue(petResponse, PetResponse.class);
	}

	public Pet toEntity(PetRequest dto) {
		Pet pet=objectMapper.convertValue(dto, Pet.class);
		PetTemperament petTemperament=new PetTemperament();
		petTemperament.setTemperament(dto.getTemperament());
		PetTemperament temperament= petTemperamentService.save(petTemperament);
		pet.setPetTemperament(temperament);
		return pet;
	}

	public void updateEntityFromDto(PetRequest request, Pet pet) {
		try {
			PetTemperament temperament=pet.getPetTemperament();
			temperament.setTemperament(request.getTemperament());
			objectMapper.updateValue(pet, request);
		}
		catch (JsonMappingException e) {
			throw new RuntimeException(e);
		}
	}

}
