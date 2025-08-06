package org.springframework.samples.petclinic.controller.restcontroller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.assembler.OwnerAssembler;
import org.springframework.samples.petclinic.dtos.ownerDto.OwnerRequest;
import org.springframework.samples.petclinic.dtos.ownerDto.OwnerResponse;
import org.springframework.samples.petclinic.exceptions.OwnerNotFoundException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.mapper.OwnerJsonMapper;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import java.util.Map;

import static org.springframework.samples.petclinic.constants.RestUrlConstants.OWNER_API_V1;
import static org.springframework.samples.petclinic.constants.RestUrlConstants.OWNER_ID;

@RestController
@RequestMapping(OWNER_API_V1)
@RequiredArgsConstructor
public class OwnerRestController {

	private final OwnerService ownerService;

	private final OwnerAssembler assembler;

	private final OwnerJsonMapper ownerJsonMapper;

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<OwnerResponse>>> getAllOwners() {
		List<Owner> owners = ownerService.findAll();
		List<EntityModel<OwnerResponse>> ownerModels = owners.stream()
			.map(ownerJsonMapper::toDto)
			.map(assembler::toModel)
			.toList();
		CollectionModel<EntityModel<OwnerResponse>> collection = CollectionModel.of(ownerModels,
				linkTo(methodOn(OwnerRestController.class).getAllOwners()).withSelfRel());
		return ResponseEntity.ok(collection);
	}

	@GetMapping(OWNER_ID)
	public ResponseEntity<EntityModel<OwnerResponse>> getOwnerById(@PathVariable Integer ownerId) {
		Owner owner = ownerService.findById(ownerId)
			.orElseThrow(() -> new OwnerNotFoundException("Owner with id {} not found" + ownerId));

		OwnerResponse dto = ownerJsonMapper.toDto(owner);
		EntityModel<OwnerResponse> model = assembler.toModel(dto);
		return ResponseEntity.ok(model);
	}

	@PostMapping
	public ResponseEntity<EntityModel<OwnerResponse>> createOwner(@RequestBody @Valid OwnerRequest ownerRequest) {
		Owner owner = ownerJsonMapper.toEntity(ownerRequest); // Map request DTO → Entity
		Owner saved = ownerService.save(owner); // Save entity
		OwnerResponse dto = ownerJsonMapper.toDto(saved); // Map entity → response DTO
		EntityModel<OwnerResponse> model = assembler.toModel(dto);

		return ResponseEntity.created(linkTo(methodOn(OwnerRestController.class).getOwnerById(saved.getId())).toUri())
			.body(model);
	}

	@PutMapping(OWNER_ID)
	public ResponseEntity<EntityModel<OwnerResponse>> updateOwner(@PathVariable Integer ownerId,
			@RequestBody @Valid OwnerRequest ownerRequest) {
		Owner existing = ownerService.findById(ownerId)
			.orElseThrow(() -> new OwnerNotFoundException("Owner with id {} not found" + ownerId));
		ownerJsonMapper.updateEntityFromDto(ownerRequest, existing);
		Owner saved = ownerService.save(existing);
		OwnerResponse dto = ownerJsonMapper.toDto(saved);
		return ResponseEntity.ok(assembler.toModel(dto));
	}

	@DeleteMapping(OWNER_ID)
	public ResponseEntity<String> deleteOwner(@PathVariable Integer ownerId) {
		ownerService.deleteById(ownerId);
		return ResponseEntity.ok("Deleted");
	}

}
