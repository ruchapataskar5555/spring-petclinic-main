package org.springframework.samples.petclinic.exceptions;

public class PetNotFoundException extends EntityNotFoundException {

	public PetNotFoundException(String message) {
		super(message);
	}

}
