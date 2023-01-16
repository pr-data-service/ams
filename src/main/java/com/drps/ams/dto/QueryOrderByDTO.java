package com.drps.ams.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@ToString
public class QueryOrderByDTO {
	
	public String fieldName;
	public boolean asc;

}
