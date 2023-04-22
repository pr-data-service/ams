package com.drps.ams.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailServiceDTO {

	public Long id;

	public String type;

	public Boolean isActive;
}
