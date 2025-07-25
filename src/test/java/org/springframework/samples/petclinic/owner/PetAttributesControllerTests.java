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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetAttributesController.class)
@DisabledInNativeImage
@DisabledInAotMode
class PetAttributesControllerTests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;


	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	@MockitoBean
	private PetTypeRepository types;

	@MockitoBean
	private PetAttributesService petAttributesService;

	@BeforeEach
	void init() {

		PetType cat = new PetType();
		cat.setId(3);
		cat.setName("hamster");
		given(this.types.findPetTypes()).willReturn(List.of(cat));

		Owner owner = new Owner();
		Pet pet = new Pet();
		PetAttributes pa=new PetAttributes();
		pet.setPetAttributes(pa);
		owner.addPet(pet);
		pet.setId(TEST_PET_ID);
		pet.setName("petty");
		pa.setTemperament("cool");
		pa.setLength(10.0);
		pa.setWeight(10.0);
		given(this.owners.findById(TEST_OWNER_ID)).willReturn(Optional.of(owner));}

	@Test
	void testInitNewPetAttributesForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/attributes", TEST_OWNER_ID, TEST_PET_ID))
			.andExpect(status().isOk())
			.andExpect(view().name("pets/createOrUpdatePetAttributesForm"));
	}

	@Test
	void testProcessNewPetAttributesForm() throws Exception {
		mockMvc
			.perform(post("/owners/{ownerId}/pets/{petId}/attributes", TEST_OWNER_ID, TEST_PET_ID)
				.param("temperament", "good")
				.param("length", String.valueOf(10.0))
				.param("weight", String.valueOf(10.0)))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/owners/{ownerId}"));
	}


}
