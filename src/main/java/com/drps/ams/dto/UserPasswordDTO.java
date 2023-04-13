package com.drps.ams.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserPasswordDTO {
		private long id;
		private String oldPassword;
		private String newPassword;
		private String confirmPassword;
}
