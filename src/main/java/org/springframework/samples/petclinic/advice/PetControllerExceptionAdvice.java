package org.springframework.samples.petclinic.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.exceptions.EntityNotFoundException;
import org.springframework.samples.petclinic.exceptions.PetNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.samples.petclinic.controller.restcontroller.PetRestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(assignableTypes = org.springframework.samples.petclinic.controller.restcontroller.PetRestController.class)
public class PetControllerExceptionAdvice {

	private static final Logger logger = LogManager.getLogger(PetControllerExceptionAdvice.class);

	@ExceptionHandler(PetNotFoundException.class)
	public ResponseEntity<String> handlePetNotFound(PetNotFoundException ex) {
		logger.error("PetNotFoundException: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}


	public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
		logger.error("EntityNotFoundException: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body("An unexpected error occurred: " + ex.getMessage());
	}

}
