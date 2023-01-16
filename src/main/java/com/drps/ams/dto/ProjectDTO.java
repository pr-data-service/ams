package com.drps.ams.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectDTO {

	ApartmentDetailsDTO apartmentDetails;
	UserDetailsDTO userDetails;
	SessionDetailsDTO sessionDetails;
	
}
