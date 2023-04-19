/**
 * 
 */
package com.drps.ams.dto;

import java.util.Date;

import com.drps.ams.annotation.EntityFieldMapping;
import com.drps.ams.annotation.EntityMapping;
import com.drps.ams.annotation.FKEntityFieldMapping;
import com.drps.ams.annotation.LinkEntityFieldMapping;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.LinkFlatDetailsAndUserDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.util.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 002ZX2744
 *
 */
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@EntityMapping(entity = FlatDetailsEntity.class)
public class FlatDetailsDTO {

	private Long id;

	private String flatNo;

	private String block;

	private Integer flatSizeSqft;

	private Integer noOfRooms;

	private Integer floorNo;

	private String flatType;
	private Long createdBy;	
	public String createdByName;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date createdDate;

	private Long modifiedBy;	
	public String modifiedByName;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = DateUtils.TIMEZONE_ASIA_KOLKATA, shape = JsonFormat.Shape.STRING)
	private Date modifiedDate;
	
	@LinkEntityFieldMapping(entity = LinkFlatDetailsAndUserDetailsEntity.class, mappingFieldL = "flatId", mappingEntity = UserDetailsEntity.class, mappingFieldR = "userId")
	private Long userId;
	
	@EntityFieldMapping(entity = UserDetailsEntity.class, name = "firstName")
	public String ownersFirstName;
	
	@EntityFieldMapping(entity = UserDetailsEntity.class, name = "lastName")
	public String ownersLastName;
}
