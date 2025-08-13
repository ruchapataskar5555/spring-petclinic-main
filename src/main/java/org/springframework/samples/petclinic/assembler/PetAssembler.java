package org.springframework.samples.petclinic.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.samples.petclinic.dtos.PetResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.samples.petclinic.constants.RestUrlConstants.OWNERS_OWNER_ID_PETS_PET_ID;

@Component
public class PetAssembler implements RepresentationModelAssembler<PetResponse, EntityModel<PetResponse>> {

	@Override
	public EntityModel<PetResponse> toModel(PetResponse dto) {
		String url = ServletUriComponentsBuilder.fromCurrentContextPath()
			.path(OWNERS_OWNER_ID_PETS_PET_ID)
			.buildAndExpand(dto.getOwnerId(), dto.getId())
			.toUriString();
		return EntityModel.of(dto, Link.of(url).withSelfRel());
	}

}
