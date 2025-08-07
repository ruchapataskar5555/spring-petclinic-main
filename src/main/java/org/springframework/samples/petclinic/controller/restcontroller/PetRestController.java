package org.springframework.samples.petclinic.controller.restcontroller;

import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.assembler.PetAssembler;

import org.springframework.samples.petclinic.dtos.petDto.PetRequest;
import org.springframework.samples.petclinic.dtos.petDto.PetResponse;
import org.springframework.samples.petclinic.exceptions.OwnerNotFoundException;
import org.springframework.samples.petclinic.exceptions.PetNotFoundException;
import org.springframework.samples.petclinic.mapper.PetJsonMapper;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.samples.petclinic.constants.RestUrlConstants.PET_API_V1;
import static org.springframework.samples.petclinic.constants.RestUrlConstants.PET_ID;

@RestController
@RequestMapping(PET_API_V1)
@RequiredArgsConstructor
public class PetRestController {

	private final PetJsonMapper petJsonMapper;

	private final PetService petService;

	private final OwnerService ownerService;

	private final PetAssembler petAssembler;

	@GetMapping(PET_ID)
	public ResponseEntity<EntityModel<PetResponse>> getPetById(@PathVariable Integer ownerId,
			@PathVariable Integer petId) {
		ownerService.findById(ownerId)
			.orElseThrow(() -> new OwnerNotFoundException("Owner not found with ID: " + ownerId));
		Pet pet = petService.findById(petId)
			.orElseThrow(() -> new PetNotFoundException("Pet not found with ID: " + petId));
		PetResponse dto = petJsonMapper.toDto(pet);
		return ResponseEntity.ok(petAssembler.toModel(dto));
	}

	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<PetResponse>>> getAllPetsForOwner(@PathVariable Integer ownerId) {
		Owner owner = ownerService.findById(ownerId)
			.orElseThrow(() -> new OwnerNotFoundException("Owner not found with ID: " + ownerId));


		List<EntityModel<PetResponse>> petModels = owner.getPets().stream().map(petJsonMapper::toDto).map(pet -> {
			EntityModel<PetResponse> petDTOEntityModel = petAssembler.toModel(pet);
			return petDTOEntityModel;
		}).toList();

		CollectionModel<EntityModel<PetResponse>> collectionModel = CollectionModel.of(petModels);
		collectionModel.add(linkTo(methodOn(PetRestController.class).getAllPetsForOwner(ownerId)).withSelfRel());
		return ResponseEntity.ok(collectionModel);
	}
	@PostMapping
	public ResponseEntity<EntityModel<PetResponse>> createPetForOwner(@PathVariable Integer ownerId,
			@RequestBody @Valid PetRequest request) {

		Owner owner = ownerService.findById(ownerId)
			.orElseThrow(() -> new OwnerNotFoundException("Owner not found with ID: " + ownerId));

		Pet pet = petJsonMapper.toEntity(request);
		pet.setOwner(owner);
		owner.getPets().add(pet);
		Owner savedOwner = ownerService.save(owner);
		Optional<Pet> first = savedOwner.getPets().stream().filter(p -> p.getName().equals(pet.getName())).findFirst();
		PetResponse petResponseDTO = petJsonMapper.toDto(first.orElse(pet));

		return ResponseEntity.created(linkTo(methodOn(OwnerRestController.class).getOwnerById(ownerId)).toUri())
			.body(petAssembler.toModel(petResponseDTO));

	}

	@PutMapping(PET_ID)
	public ResponseEntity<EntityModel<PetResponse>> updatePet(@PathVariable Integer petId,
			@RequestBody @Valid PetRequest request) {
		Pet existingPet = petService.findById(petId)
			.orElseThrow(() -> new PetNotFoundException("Owner not found with ID: " + petId));

		Pet updatedPet=petJsonMapper.updateEntityFromDto(request, existingPet);

		PetResponse dto = petJsonMapper.toDto(updatedPet);
		return ResponseEntity.ok(petAssembler.toModel(dto));
	}

	@DeleteMapping(PET_ID)
	public ResponseEntity<String> deletePet(@PathVariable Integer petId) {
		petService.findById(petId).orElseThrow(() -> new PetNotFoundException("Pet with id {} not found" + petId));
		petService.deleteById(petId);
		return ResponseEntity.ok("Deleted");
	}

}
