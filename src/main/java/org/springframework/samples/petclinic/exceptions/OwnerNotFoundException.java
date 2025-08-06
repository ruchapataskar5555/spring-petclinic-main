package org.springframework.samples.petclinic.exceptions;

import org.springframework.samples.petclinic.model.Owner;

public class OwnerNotFoundException extends RuntimeException {

	public OwnerNotFoundException(String msg) {
		super(msg);
	}

}
