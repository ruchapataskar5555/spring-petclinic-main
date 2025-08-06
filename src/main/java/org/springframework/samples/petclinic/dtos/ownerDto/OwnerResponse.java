package org.springframework.samples.petclinic.dtos.ownerDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.samples.petclinic.dtos.petDto.PetResponse;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OwnerResponse {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("address")
	private String address;

	@JsonProperty("city")
	private String city;

	@JsonProperty("telephone")
	private String telephone;

	@JsonProperty("pets")
	private List<PetResponse> pets;

}
