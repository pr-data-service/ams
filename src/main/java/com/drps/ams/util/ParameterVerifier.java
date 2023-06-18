package com.drps.ams.util;

import java.util.Arrays;
import java.util.Objects;

public class ParameterVerifier {
	
	public static final String STRING_UNDEFINED = "undefined";
	public static final String STRING_NULL = "null";
	public static final String STRING_NAN = "NaN";
	public static final String STRING_EMPTY = "";
	
	public static boolean isValidParam(Object arg) {
		if(Objects.isNull(arg) || STRING_UNDEFINED.equals(arg) || STRING_NAN.equals(arg) || STRING_NULL.equals(arg)) {
			return false;
		}
		return true;
	}
	
	public static boolean isEmpty(Object arg) {
		try {
			String str = getString(arg);
			if(str.length() == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isBlank(Object arg) {
		try {
			String str = getString(arg);
			if(str.length() == 0 || str.replaceAll(" ", "").length() == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getString(Object arg) {
		String str = STRING_EMPTY;
		try {
			if(isValidParam(arg)) {
				str = String.valueOf(arg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static int getInteger(Object arg) {
		int no = 0;
		try {
			if(isValidParam(arg)) {
				no = Integer.valueOf(String.valueOf(arg));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return no;
	}
	
	public static boolean isBoolean(Object value) {
	    return value != null && Arrays.stream(new String[]{"true", "false", "1", "0"})
	            .anyMatch(b -> b.equalsIgnoreCase(String.valueOf(value)));
	}
	
	public static Object getArrayValueByIndex(int index, Object ...obj) {
		try {
			return obj[index];
		} catch (Exception e) {
			return null;
		}
	}

}
