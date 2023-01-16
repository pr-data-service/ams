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
public class ListViewFieldDTO {

	public String dataField;
	public String type;
	public String keyConstraints;
	public boolean hidden;
	public boolean sort;
	
	public Object value;
}
