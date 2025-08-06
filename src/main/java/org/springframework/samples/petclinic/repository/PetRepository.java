package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.samples.petclinic.model.Pet;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Integer> {

	@Override
	Optional<Pet> findById(Integer integer);

}
