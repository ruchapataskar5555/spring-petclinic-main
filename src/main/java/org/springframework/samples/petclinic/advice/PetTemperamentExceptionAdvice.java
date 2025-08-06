package org.springframework.samples.petclinic.advice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.samples.petclinic.exceptions.PetNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(assignableTypes = org.springframework.samples.petclinic.controller.PetTemperamentMVCController.class)
public class PetTemperamentExceptionAdvice {

	private static final Logger logger = LogManager.getLogger(PetTemperamentExceptionAdvice.class);

	@ExceptionHandler(PetNotFoundException.class)
	public String handlePetNotFound(PetNotFoundException ex, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.error("PetNotFoundException: {}", ex.getMessage());
		redirectAttributes.addFlashAttribute("error", ex.getMessage());
		return "redirect:" + request.getHeader("Referer");
	}

	@ExceptionHandler(Exception.class)
	public String handleGenericException(Exception ex, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		logger.error("Unexpected error in PetTemperamentController", ex);
		redirectAttributes.addFlashAttribute("error", "An unexpected error occurred. Please try again.");
		return "redirect:" + request.getHeader("Referer");
	}

}
