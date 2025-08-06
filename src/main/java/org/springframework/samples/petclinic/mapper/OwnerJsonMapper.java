package org.springframework.samples.petclinic.mapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.samples.petclinic.dtos.ownerDto.OwnerRequest;
import org.springframework.samples.petclinic.dtos.ownerDto.OwnerResponse;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerJsonMapper {

	private final ObjectMapper objectMapper;

	public OwnerJsonMapper() {
		this.objectMapper = new ObjectMapper();
		this.objectMapper.registerModule(new JavaTimeModule());
		this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public OwnerResponse toDto(Owner owner) {
		if (owner == null) {
			return null;
		}
		return objectMapper.convertValue(owner, OwnerResponse.class);
	}

	public Owner toEntity(OwnerRequest dto) {
		return objectMapper.convertValue(dto, Owner.class);
	}

	public void updateEntityFromDto(OwnerRequest request, Owner owner) {
		try {
			objectMapper.updateValue(owner, request); // Merges fields from request into
														// existing owner
		}
		catch (JsonMappingException e) {
			throw new RuntimeException(e);
		}
	}

}
