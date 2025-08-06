package org.springframework.samples.petclinic.constants;

public class MvcUrlConstants {

	public static final String PET_API_VERSION = "/api/v1";

	public static final String BASE_PET_API = "/owners/{ownerId}/pets/{petId}";

	public static final String GET_PET_TEMPERAMENT = PET_API_VERSION + BASE_PET_API + "/attributes";

	public static final String POST_PET_TEMPERAMENT = PET_API_VERSION + BASE_PET_API + "/attributes";

	;
	public static final String PUT_PET_TEMPERAMENT = PET_API_VERSION + BASE_PET_API + "/attributes";

	;
	public static final String DELETE_PET_TEMPERAMENT = PET_API_VERSION + BASE_PET_API + "/attributes";

	;

}
