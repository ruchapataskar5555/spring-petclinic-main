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
package org.springframework.samples.petclinic.owner;

import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

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
class PetAttributesController {

	private static final Logger logger = LogManager.getLogger(PetAttributesController.class);
	private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";
	private final OwnerRepository owners;
	private final PetAttributesService petAttrService;

	public PetAttributesController(OwnerRepository owners,PetAttributesService petAttrService)
	{
		this.owners = owners;
		this.petAttrService=petAttrService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}


	@ModelAttribute("petAttributes")
	public PetAttributes loadPetWithAttributes(@PathVariable("ownerId") int ownerId, @PathVariable("petId") int petId,
											   Map<String, Object> model) {
		Optional<Owner> optionalOwner = owners.findById(ownerId);
		Owner owner = optionalOwner.orElseThrow(() -> new IllegalArgumentException(
			"Owner not found with id: " + ownerId + ". Please ensure the ID is correct "));

		Pet pet = owner.getPet(petId);
		model.put("pet", pet);
		model.put("owner", owner);

		PetAttributes attributes = pet.getPetAttributes() != null ? pet.getPetAttributes() : new PetAttributes();
		model.put("petAttributes", attributes);
		pet.setPetAttributes(new PetAttributes());
		return attributes;
	}


	@GetMapping("/owners/{ownerId}/pets/{petId}/attributes")
	public String initNewPetAttributesForm(@ModelAttribute Owner owner,@PathVariable("petId") int petId, Model model) {
		logger.info("Getting pet with ID: {}", petId);
		Pet pet = owner.getPet(petId);
		if(pet==null){
			logger.error(" pet with ID: {} not found", petId);
			return "redirect:/owners/{ownerId}";
		}
		PetAttributes attributes = pet.getPetAttributes() != null ? pet.getPetAttributes() : new PetAttributes();
		model.addAttribute("petAttributes", attributes);
		return "pets/createOrUpdatePetAttributesForm";
	}

	@PostMapping("/owners/{ownerId}/pets/{petId}/attributes")
	public String processNewPetAttributesForm(@PathVariable("ownerId") int ownerId,
											  @PathVariable("petId") int petId,
											  @ModelAttribute("petAttributes") PetAttributes petAttributes,
											  @ModelAttribute Owner owner,
											  RedirectAttributes redirectAttributes) {

		Pet pet=owner.getPet(petId);
		if(pet==null){
			logger.error(" The pet with ID: {} not found", petId);
			redirectAttributes.addFlashAttribute("error","Pet Not Found");
			return "redirect:/owners/{ownerId}";
		}

		if(pet.getPetAttributes()!=null && pet.getPetAttributes().getId()!=null){
			redirectAttributes.addFlashAttribute("error","Pet Attributes already present");
			return "redirect:/owners/{ownerId}";
		}
		petAttrService.save(petAttributes);
		pet.setPetAttributes(petAttributes);
		owner.addPetAttributes(pet.getId(),petAttributes);
		this.owners.save(owner);
		logger.info("Pet attributes successfully saved for pet ID: {}of owner {}", petId, ownerId);
		redirectAttributes.addFlashAttribute("message", "Your Attributes have been added");

		return "redirect:/owners/{ownerId}";
	}


	@PutMapping("/owners/{ownerId}/pets/{petId}/attributes")
	public String updateAttributes(@PathVariable("ownerId") int ownerId,
								   @PathVariable("petId") int petId,
								   @ModelAttribute("petAttributes") PetAttributes petAttributes,
								   @ModelAttribute Owner owner,
								   RedirectAttributes redirectAttributes){
		Pet pet = owner.getPet(petId);
		if (pet == null) {
			logger.error("Pet with ID: {} not found", petId);
			redirectAttributes.addFlashAttribute("error","Pet Not Found");
			return "redirect:/owners/{ownerId}";

		}

		PetAttributes oldPetAttributes=pet.getPetAttributes(); // Preserve existing ID
		oldPetAttributes.setTemperament(petAttributes.getTemperament());
		oldPetAttributes.setWeight(petAttributes.getWeight());
		oldPetAttributes.setLength(petAttributes.getLength());
		petAttrService.save(oldPetAttributes);
		pet.setPetAttributes(oldPetAttributes);
		this.owners.save(owner);
		logger.info("Pet attributes successfully updated or saved for pet ID: {}of owner {}", petId, ownerId);
		return "redirect:/owners/{ownerId}";
	}


	@DeleteMapping("/owners/{ownerId}/pets/{petId}/attributes")
	public String deletePetAttributes(@ModelAttribute Owner owner, @PathVariable("petId") int petId, RedirectAttributes redirectAttributes) {
		Pet pet=owner.getPet(petId);
		this.petAttrService.deleteById(pet.getPetAttributes().getId());
		pet.setPetAttributes(null);
		owner.addPet(pet);
		owners.save(owner);
		logger.info("Pet attributes successfully deleted for pet ID: {}of owner {}", petId, owner.getId());

		return "redirect:/owners/{ownerId}";
	}
}
