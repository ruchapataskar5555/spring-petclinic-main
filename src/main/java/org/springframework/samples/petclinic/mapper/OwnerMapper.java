package org.springframework.samples.petclinic.mapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.samples.petclinic.dtos.OwnerRequest;
import org.springframework.samples.petclinic.dtos.OwnerResponse;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OwnerMapper {

	private final ModelMapper modelMapper;

	public Owner toEntity(OwnerRequest dto) {
		Owner owner = modelMapper.map(dto, Owner.class);
		return owner;
	}

	public OwnerResponse toDto(Owner owner) {
		OwnerResponse dto = modelMapper.map(owner, OwnerResponse.class);
		return dto;
	}

	public void updateEntityFromDto(@Valid OwnerRequest dto, Owner existingOwner) {
		modelMapper.map(dto, existingOwner);
	}

}
