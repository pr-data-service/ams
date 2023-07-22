package com.drps.ams.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.drps.ams.bean.UserContext;
import com.drps.ams.entity.LinkFlatDetailsAndUserDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;

public class ApiConstants {
	
	public static final String STRING_TIMEZONE_ID_IST = "IST";
	public static final String STRING_TIMEZONE_ID_UTC = "UTC";
	
	public static final int RESP_STATUS_SUCCESS = 100;	
	public static final int RESP_STATUS_FAIL = 0;
	public static final int RESP_STATUS_EXCEPTION = 200;	
	public static final int RESP_STATUS_USER_DISABLED_EXCEPTION = 201;
	public static final int RESP_STATUS_INVALID_CREDENTIALS_EXCEPTION = 202;
	public static final int RESP_STATUS_USER_CONTEXT_NOT_FOUND_EXCEPTION = 203;
	public static final int RESP_STATUS_INVALID_CONFIRM_PASSWORD_EXCEPTION = 204;
	
	public static final int RESP_STATUS_NO_RECORD_FOUND_EXCEPTION = 220;
	public static final int RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION = 221;
	public static final int RESP_STATUS_DUPLICATE_RECORD_EXCEPTION = 222;
	public static final int RESP_STATUS_INVALID_EMAIL_EXCEPTION = 223;
	public static final int RESP_STATUS_RECORD_CANCELED_EXCEPTION = 224;
	
	public static final int RESP_STATUS_UNSUPPORTED_MEDIA_TYPE = 230;
	
	private static final String RESP_STATUS_MSG_SUCCESS = "SUCCESS";
	private static final String RESP_STATUS_MSG_NO_RECORD_FOUND = "Record not found";
	private static final String RESP_STATUS_MSG_RECORD_ID_NOT_FOUND = "Record id not found";
	private static final String RESP_STATUS_MSG_DUPLICATE_RECORD = "Duplicate Record found";
	private static final String RESP_STATUS_MSG_USER_CONTEXT_NOT_FOUND = "User Context not found";
	private static final String RESP_STATUS_MSG_INVALID_CONFIRM_PASSWORD = "New password and confirm password should be same.";
	private static final String RESP_STATUS_MSG_INVALID_CREDENTIALS = "Invalid credentials.";
	private static final String RESP_STATUS_MSG_RECORD_CANCELED = "Record canceled";
	private static final String RESP_STATUS_MSG_UNSUPPORTED_MEDIA_TYPE = "Unsupported media type";
	
	public static final Map<Integer, String> STATUS_MESSAGE = new HashMap<>();
	static {
		
		STATUS_MESSAGE.put(RESP_STATUS_SUCCESS, RESP_STATUS_MSG_SUCCESS);
		STATUS_MESSAGE.put(RESP_STATUS_USER_CONTEXT_NOT_FOUND_EXCEPTION, RESP_STATUS_MSG_USER_CONTEXT_NOT_FOUND);
		
		STATUS_MESSAGE.put(RESP_STATUS_NO_RECORD_FOUND_EXCEPTION, RESP_STATUS_MSG_NO_RECORD_FOUND);
		STATUS_MESSAGE.put(RESP_STATUS_RECORD_ID_NOT_FOUND_EXCEPTION, RESP_STATUS_MSG_RECORD_ID_NOT_FOUND);
		STATUS_MESSAGE.put(RESP_STATUS_DUPLICATE_RECORD_EXCEPTION, RESP_STATUS_MSG_DUPLICATE_RECORD);
		STATUS_MESSAGE.put(RESP_STATUS_INVALID_CONFIRM_PASSWORD_EXCEPTION, RESP_STATUS_MSG_INVALID_CONFIRM_PASSWORD);
		STATUS_MESSAGE.put(RESP_STATUS_INVALID_CREDENTIALS_EXCEPTION, RESP_STATUS_MSG_INVALID_CREDENTIALS);
		STATUS_MESSAGE.put(RESP_STATUS_RECORD_CANCELED_EXCEPTION, RESP_STATUS_MSG_RECORD_CANCELED);
		STATUS_MESSAGE.put(RESP_STATUS_UNSUPPORTED_MEDIA_TYPE, RESP_STATUS_MSG_UNSUPPORTED_MEDIA_TYPE);
	}
	
	public static final String OBJECT_USER_DETAILS = "USER_DETAILS";
	public static final String OBJECT_FLAT_DETAILS = "FLAT_DETAILS";
	public static final String OBJECT_EVENTS = "EVENTS";
	public static final String OBJECT_MAINTENANCE = "MAINTENANCE";
	public static final String OBJECT_PAYMENT = "PAYMENT";
	public static final String OBJECT_PAYMENT_DETAILS = "PAYMENT_DETAILS";
	public static final String OBJECT_EXPENSE = "EXPENSE";
	public static final String OBJECT_EXPENSE_ITEMS = "EXPENSE_ITEMS";
	public static final String OBJECT_EXPENSE_NOTES = "NOTES";
	
	public static final List<String> OBJECT_LIST = 
			Arrays.asList(OBJECT_USER_DETAILS, OBJECT_FLAT_DETAILS, OBJECT_EVENTS,
						OBJECT_MAINTENANCE, OBJECT_PAYMENT, OBJECT_PAYMENT_DETAILS,
						OBJECT_EXPENSE, OBJECT_EXPENSE_ITEMS, OBJECT_EXPENSE_NOTES);

	
	
	public static final String LINK_OBJECT_MAINTENANCE = "link_flat_details_user_details";
	
	public static final Map<String, Class> LINK_OBJECT_ENTITY_MAP = new HashMap<>();
	static {
		LINK_OBJECT_ENTITY_MAP.put(LINK_OBJECT_MAINTENANCE, LinkFlatDetailsAndUserDetailsEntity.class);
	}
	
	
	public static <T> T createInstByTableName(String tableName) throws InstantiationException, IllegalAccessException {
		if(tableName != null) {
			tableName = tableName.toLowerCase();
			Class<?> clss = ApiConstants.LINK_OBJECT_ENTITY_MAP.get(tableName);
			return clss != null ? (T) clss.newInstance() : null;
		}
		return null;		
	}
	
	public static final long INVALID_RECORD_ID = -9999;
	public static final long SYSTEM_USER_ID = -99999;
	public static final long LIST_OPTION_SELECT_VAL = -99999;
	
	private static final String SYSTEM_USER_NAME = "System";
	public static final String DEFAULT_EVENT_NAME = "MAINTENANCE";
	
	public static String getSystemUserName(long id) {
		return id == SYSTEM_USER_ID ? SYSTEM_USER_NAME : "";
	}
	
	public static Long getSAdminUserId(UserDetailsEntity userDetailsEntity) {
		if(userDetailsEntity != null && USER_ROLE_SADMIN.equals(userDetailsEntity.getRole())) {
			return userDetailsEntity.getId();
		} else {
			throw new RuntimeException("Invalid SAdmin.");
		}
	}
	
	public static String getSAdminUserName(UserDetailsEntity userDetailsEntity) {
		if(userDetailsEntity != null && USER_ROLE_SADMIN.equals(userDetailsEntity.getRole())) {
			return userDetailsEntity.getFirstName();
		} else {
			throw new RuntimeException("Invalid SAdmin.");
		}
	}
	
	public static final List<String> DEFAULT_FIELDS_NOT_TO_MODIFY_ON_UPDATE = Arrays.asList(new String[] {"sessionId", "apartmentId", "isDeleted", "isActive", "createdBy", "createdDate", "modifiedBy", "modifiedDate"});
	
	
	public static final String EVENT_TYPE_REGULAR = "REGULAR";
	public static final String EVENT_TYPE_ONETIME = "ONETIME";
	public static final String EVENT_TYPE_SUBEVENT = "SUBEVENT";
	
	
	
	public static final String NOTE_TYPE_NOTE = "NOTE";
	public static final String NOTE_TYPE_SYSTEM = "SYSTEM";

	public static final String FLAT_TYPE_SINGLE = "SINGLE";
	public static final String FLAT_TYPE_DOUBLE = "DOUBLE";
	
	public static final String PAYMENT_MODE_CASH = "CASH";
	public static final String PAYMENT_MODE_ONLINE = "ONLINE";
	public static final String PAYMENT_MODE_CHEQUE = "CHEQUE";
	
	public static final String SADMIN_USER_NAME = "Super Admin";
	
	public static final String USER_ROLE_SADMIN = "SADMIN";
	public static final String USER_ROLE_USER = "USER";
	public static final String USER_ROLE_ADMIN = "ADMIN";
	public static final String USER_ROLE_SECRETARY = "SECRETARY";
	public static final String USER_ROLE_ASST_SECRETARY = "ASST-SECRETARY";
	public static final String USER_ROLE_TREASURER = "TREASURER";
	public static final String USER_ROLE_ASST_TREASURER = "ASST-TREASURER";
	
	public static final String SADMIN_CONTACT_NO = "1111111111";
	public static final String USER_DEFAULT_PASSWORD = "pass";
	
	public static final String USER_TYPE_OWNER = "OWNER";
	public static final String USER_TYPE_USER = "USER";
	
	public static final String USER_PERMISSION_CREATE = "CREATE";
	public static final String USER_PERMISSION_VIEW = "VIEW";
	public static final String USER_PERMISSION_EDIT = "EDIT";
	public static final String USER_PERMISSION_DELETE = "DELETE";
	
	
}
