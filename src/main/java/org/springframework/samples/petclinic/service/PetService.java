package org.springframework.samples.petclinic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.dtos.petDto.PetRequest;
import org.springframework.samples.petclinic.exceptions.PetNotFoundException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetTemperament;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;

import java.util.List;
import java.util.Optional;


public interface PetService {

	public List<Pet> findAll();
	public Optional<Pet> findById(Integer id) ;
	public void deleteById(Integer id);
	public Pet save(Pet pet) ;

}
