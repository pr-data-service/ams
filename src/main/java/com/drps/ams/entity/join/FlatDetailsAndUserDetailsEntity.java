package com.drps.ams.entity.join;

import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.LinkFlatDetailsAndUserDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlatDetailsAndUserDetailsEntity {
	
	LinkFlatDetailsAndUserDetailsEntity linkFlatDetailsAndUserDetailsEntity;
	FlatDetailsEntity flatDetails;
	UserDetailsEntity userDetails;
	
}
