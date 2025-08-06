/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.samples.petclinic.exceptions.PetNotFoundException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetTemperament;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.service.PetTemperamentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

import static org.springframework.samples.petclinic.constants.MvcUrlConstants.*;
import static org.springframework.samples.petclinic.constants.ViewConstants.OWNER_DETAILS;
import static org.springframework.samples.petclinic.constants.ViewConstants.PET_TEMPERAMENT_VIEW;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Dave Syer
 * @author Wick Dynex
 * @author Rucha Pataskar
 */
@Controller
public class PetTemperamentMVCController {

	private static final Logger logger = LogManager.getLogger(PetTemperamentMVCController.class);

	private final OwnerRepository owners;

	private final PetTemperamentService petTemperamentService;

	public PetTemperamentMVCController(OwnerRepository owners, PetTemperamentService petTemperamentService) {
		this.owners = owners;
		this.petTemperamentService = petTemperamentService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@ModelAttribute("petTemperament")
	public PetTemperament loadPetWithAttributes(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			Map<String, Object> model) {
		Optional<Owner> optionalOwner = owners.findById(ownerId);
		Owner owner = optionalOwner.orElseThrow(() -> new IllegalArgumentException(
				"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));

		Pet pet = owner.getPet(petId);
		model.put("pet", pet);
		model.put("owner", owner);

		PetTemperament attributes = pet.getPetTemperament() != null ? pet.getPetTemperament() : new PetTemperament();
		model.put("petTemperament", attributes);
		pet.setPetTemperament(new PetTemperament());
		return attributes;
	}

	@GetMapping(GET_PET_TEMPERAMENT)
	public String initNewPetAttributesForm(@ModelAttribute Owner owner, @PathVariable("petId") int petId, Model model) {
		logger.info("Getting pet with ID: {}", petId);
		Pet pet = owner.getPet(petId);
		if (pet == null) {
			logger.error(" pet with ID: {} not found", petId);
			return OWNER_DETAILS;
		}
		PetTemperament attributes = pet.getPetTemperament() != null ? pet.getPetTemperament() : new PetTemperament();
		model.addAttribute("petTemperament", attributes);
		return PET_TEMPERAMENT_VIEW;
	}

	@PostMapping(POST_PET_TEMPERAMENT)
	public String processNewPetAttributesForm(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@ModelAttribute("petTemperament") PetTemperament petTemperament, @ModelAttribute Owner owner,
			RedirectAttributes redirectAttributes) {

		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new PetNotFoundException("pet with id {} not found" + petId);
		}

		petTemperamentService.save(petTemperament);
		pet.setPetTemperament(petTemperament);
		this.owners.save(owner);
		logger.info("Pet attributes successfully saved for pet ID: {}of owner {}", petId, ownerId);
		redirectAttributes.addFlashAttribute("message", "Your Attributes have been added");

		return OWNER_DETAILS;
	}

	@PutMapping(PUT_PET_TEMPERAMENT)
	public String updateAttributes(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
			@ModelAttribute("petTemperament") PetTemperament petTemperament, @ModelAttribute Owner owner,
			RedirectAttributes redirectAttributes) {
		Pet pet = owner.getPet(petId);
		if (pet == null) {
			throw new PetNotFoundException("pet with id {} not found" + petId);
		}

		PetTemperament oldPetTemperament = pet.getPetTemperament(); // Preserve existing
																	// ID
		oldPetTemperament.setTemperament(petTemperament.getTemperament());
		petTemperamentService.save(oldPetTemperament);
		pet.setPetTemperament(oldPetTemperament);
		this.owners.save(owner);
		logger.info("Pet attributes successfully updated or saved for pet ID: {}of owner {}", petId, ownerId);
		return OWNER_DETAILS;
	}

	@DeleteMapping(DELETE_PET_TEMPERAMENT)
	public String deletePetAttributes(@ModelAttribute Owner owner, @PathVariable("petId") int petId,
			RedirectAttributes redirectAttributes) {
		Pet pet = owner.getPet(petId);
		this.petTemperamentService.deleteById(pet.getPetTemperament().getId());
		owner.addPet(pet);
		owners.save(owner);
		logger.info("Pet attributes successfully deleted for pet ID: {}of owner {}", petId, owner.getId());

		return OWNER_DETAILS;
	}

}
