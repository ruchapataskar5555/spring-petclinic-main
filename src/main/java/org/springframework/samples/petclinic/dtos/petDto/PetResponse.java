package org.springframework.samples.petclinic.dtos.petDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetResponse {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("birthDate")
	private String birthDate;

	@JsonProperty("type")
	private PetTypeRequest type;

	@JsonProperty("temperament")
	private String temperament;

	@JsonProperty("length")
	private Double length;

	@JsonProperty("weight")
	private Double weight;

	@JsonProperty("ownerId")
	private Integer ownerId;

}
