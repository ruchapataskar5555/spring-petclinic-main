package org.springframework.samples.petclinic.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OwnerServiceImpl implements OwnerService {

	private final OwnerRepository ownerRepository;

	public List<Owner> findAll() {
		return ownerRepository.findAll();
	}

	public Optional<Owner> findById(Integer id) {
		return Optional.ofNullable(ownerRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("Owner not found with ID: " + id)));
	}

	public Owner save(Owner owner) {
		return ownerRepository.save(owner);
	}

	public void deleteById(Integer id) {
		if (!ownerRepository.existsById(id)) {
			throw new EntityNotFoundException("Owner not found with ID: " + id);
		}
		ownerRepository.deleteById(id);
	}

}
