package org.springframework.samples.petclinic.exceptions.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.controller.restcontroller.OwnerRestController;
import org.springframework.samples.petclinic.exceptions.EntityNotFoundException;
import org.springframework.samples.petclinic.exceptions.OwnerNotFoundException;
import org.springframework.samples.petclinic.exceptions.PetNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.samples.petclinic.controller.restcontroller.PetRestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.samples.petclinic.constants.ErrorConstants.*;

@RestControllerAdvice(assignableTypes = { PetRestController.class, OwnerRestController.class })
public class RestControllerExceptionHandler {

	private static final Logger logger = LogManager.getLogger(RestControllerExceptionHandler.class);

	@ExceptionHandler(PetNotFoundException.class)
	public ResponseEntity<String> handlePetNotFound(PetNotFoundException ex) {
		logger.error("PetNotFoundException: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(OwnerNotFoundException.class)
	public ResponseEntity<String> handlePetNotFound(OwnerNotFoundException ex) {
		logger.error("OwnerNotFoundException: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
		List<Map<String, String>> fieldErrors = ex.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(error -> Map.of(FIELD, error.getField(), MESSAGE, error.getDefaultMessage()))
			.toList();

		return buildResponse(HttpStatus.BAD_REQUEST, VALIDATION_FAILED, INPUT_VALIDATION_ERROR, ex,
			Map.of(ERROR, fieldErrors));
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

	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message,
															  Exception ex, Map<String, Object> extraFields) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put(TIMESTAMP, LocalDateTime.now());
		body.put(STATUS, status.value());
		body.put(ERROR, error);
		body.put(MESSAGE, message);
		body.put(EXCEPTION, ex.getClass().getSimpleName());
		body.putAll(extraFields);
		return new ResponseEntity<>(body, status);
	}

}
