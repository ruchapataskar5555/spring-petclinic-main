package org.springframework.samples.petclinic.constants;

public class RestUrlConstants {

	public static final String OWNER_API_V1 = "/api/v1/owners";

	public static final String OWNER_ID = "/{ownerId}";

	public static final String PET_ID = "/{petId}";

	public static final String OWNERS_OWNER_ID_PETS_PET_ID = "/v1/owners/{ownerId}/pets/{petId}";

	public static final String PET_API_V1 = "/api/v1/owners/{ownerId}/pets";

}
