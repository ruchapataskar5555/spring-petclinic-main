package org.springframework.samples.petclinic.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface OwnerService {


	public List<Owner> findAll();

	public Optional<Owner> findById(Integer id) ;

	public Owner save(Owner owner);

	public void deleteById(Integer id) ;
}
