package org.springframework.samples.petclinic.dtos.petDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetTypeResponse {
	private Integer id;
	private String name;
}
