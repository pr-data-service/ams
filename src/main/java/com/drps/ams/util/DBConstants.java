package com.drps.ams.util;

import java.util.HashMap;
import java.util.Map;

public class DBConstants {
	
	public static final String FIELD_TYPE_TEXT = "TEXT";
	public static final String FIELD_TYPE_NUMBER = "NUMBER";
	public static final String FIELD_TYPE_BOOLEAN = "BOOLEAN";
	public static final String FIELD_TYPE_DATE= "DATE";
	
	
	public static final String FIELD_NAME_FLAT_NO = "flatNo";
	public static final String FIELD_NAME_FK_FLAT_ID = "flatId";
	public static final Map<String, String> DATABASE_KEY_CONSTRAINTS_MAP = new HashMap<>();
	static {		
		DATABASE_KEY_CONSTRAINTS_MAP.put(FIELD_NAME_FLAT_NO, FIELD_NAME_FK_FLAT_ID);
		
	}
	
	
	

}
