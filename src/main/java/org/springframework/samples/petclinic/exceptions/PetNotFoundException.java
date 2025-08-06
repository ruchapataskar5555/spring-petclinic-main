package org.springframework.samples.petclinic.exceptions;

public class PetNotFoundException extends RuntimeException {

	public PetNotFoundException(String message) {
		super(message);
	}

}
