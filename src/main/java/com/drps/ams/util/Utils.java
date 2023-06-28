package com.drps.ams.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.FileCopyUtils;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.entity.UserRolePermissionEntity;
import com.drps.ams.exception.UserContextNotFoundException;

public class Utils {
	
	public static UserContext getUserContext() {
		Authentication obj = SecurityContextHolder.getContext().getAuthentication();
		UserContext cntx = (UserContext)obj.getPrincipal();
		if(cntx == null) {
			throw new UserContextNotFoundException(ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_USER_CONTEXT_NOT_FOUND_EXCEPTION));
		}
		return cntx;
	}
	
    // Method to generate a random alphanumeric password of a specific length
    public static String generateRandomPassword(int len) {
        // ASCII range – alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
 
        SecureRandom random = new SecureRandom();
 
        // each iteration of the loop randomly chooses a character from the given
        // ASCII range and appends it to the `StringBuilder` instance
        return IntStream.range(0, len)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }
    
    public static String[] getIgnoreEntityPropsOnUpdate(String[] fields) {
    	List<String> list = new ArrayList<String>(ApiConstants.DEFAULT_FIELDS_NOT_TO_MODIFY_ON_UPDATE);
    	if(fields != null) {
    		list.addAll(Arrays.asList(fields));
    	}    	
    	return list.toArray(new String[0]);
    }
    
    
    
    public static <T,M> List<T> convertList(List<M> list, Class<T> cls) {
    	List<T> rtnList = new ArrayList<>();
    	try {
    		if(list != null && !list.isEmpty() && cls != null) {    			
        		T t = null;
        		for(M m : list) {
        			t = cls.newInstance();
        			BeanUtils.copyProperties(m, t);
    				rtnList.add(t);
        		}
        	}
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage());
		}    	
		return rtnList;
    }
    
    public static <T, M> T copyProps(M m, Class<T> cls, boolean isIgonreProps) {
    	T t = null;
    	try {
    		if(m != null) {
        		t = cls.newInstance();
        		if(isIgonreProps) {
        			BeanUtils.copyProperties(m, t, Utils.getIgnoreEntityPropsOnUpdate(null));
        		} else {
        			BeanUtils.copyProperties(m, t);
        		}        		
        	}
    	} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return t;
    }
    
    public static String getUserFullName(UserDetailsEntity userDetailsEntity) {
		if(userDetailsEntity != null) {
			String firstName = userDetailsEntity.getFirstName() != null ? userDetailsEntity.getFirstName() : "";
			String lastName = userDetailsEntity.getLastName() != null ? userDetailsEntity.getLastName() : "";
			String userFullName = firstName + " "+ lastName;
			return userFullName.trim();
		}
		return "";
	}
    
    public static Map<String, Object> convertArrayToMap(Object values[], String fieldNames[]) {
    	Map<String, Object> map = new HashMap<>();
    	if(values != null && fieldNames != null && values.length == fieldNames.length) {
    		
    		int pos = 0;
    		for(String fldNm : fieldNames) {
    			map.put(fldNm, values[pos++]);
    		}
    	}
    	return map;
    }
    
    public static String getFKField(String fieldName, String keyConstraints) {
    	if("FK".equalsIgnoreCase(keyConstraints)) {
    		return DBConstants.DATABASE_KEY_CONSTRAINTS_MAP.getOrDefault(fieldName, null);
    	}
    	return null;
    }
    
    public static String wordCamelCase(String str) {
    	return Arrays.asList(str.split(" ")).stream().map( input -> Character.toLowerCase(input.charAt(0)) +
                (input.length() > 1 ? input.substring(1) : "")).collect(Collectors.joining(" "));
    }
    
    public static void downloadPdfFile(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
    	
    	if (file != null && file.exists()) {

			//get the mimetype
			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				//unknown mimetype so set the mimetype to application/octet-stream
				mimeType = "application/octet-stream";
			}

			response.setContentType(mimeType);

			/**
			 * In a regular HTTP response, the Content-Disposition response header is a
			 * header indicating if the content is expected to be displayed inline in the
			 * browser, that is, as a Web page or as part of a Web page, or as an
			 * attachment, that is downloaded and saved locally.
			 * 
			 */

			/**
			 * Here we have mentioned it to show inline
			 */
			response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

			 //Here we have mentioned it to show as attachment
			 //response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));

			response.setContentLength((int) file.length());

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			FileCopyUtils.copy(inputStream, response.getOutputStream());
		}
    }
    
    public static SessionDetailsEntity getSessionDetailsByYearAndMonth(List<SessionDetailsEntity> sessionList, int year, int month ) {
    	LocalDate mntDt = LocalDate.of(year, month, 1);
		
		SessionDetailsEntity sessionDetailsEntity = sessionList.stream().filter( f -> {
			LocalDate frmDt = new java.sql.Date(f.getFromDate().getTime()).toLocalDate();
			LocalDate toDt = new java.sql.Date(f.getToDate().getTime()).toLocalDate();
			
			return mntDt.isEqual(frmDt) || ( mntDt.isAfter(frmDt) && mntDt.isBefore(toDt) ) ? true : false;
		}).findAny().orElse(null);
		
		return sessionDetailsEntity;
    }
    
    public static SessionDetailsEntity getPreviousSessionDetailsByCurrentSessionFromDate(List<SessionDetailsEntity> sessionList, Date fromDate ) {
    	SessionDetailsEntity prevSessionDetailsEntity = sessionList.stream().filter( f -> {
			LocalDate curntFrmDt = new java.sql.Date(fromDate.getTime()).toLocalDate();
			curntFrmDt = curntFrmDt.minusDays(1);
			LocalDate frmDt = new java.sql.Date(f.getFromDate().getTime()).toLocalDate();
			LocalDate toDt = new java.sql.Date(f.getToDate().getTime()).toLocalDate();
			
			return curntFrmDt.isEqual(toDt) || ( curntFrmDt.isAfter(frmDt) && curntFrmDt.isBefore(toDt) ) ? true : false;
		}).findAny().orElse(null);
		
		return prevSessionDetailsEntity;
    }
    
    public static boolean isDueTillCurrentDateOrLessThan(PaymentDetailsDTO dto) {
    	LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
		int curYear = currentDate.getYear();
		int curMonth = currentDate.getMonthValue();
		if(curYear > dto.getPaymentYear()) {
			return true;
		} else if(curYear == dto.getPaymentYear() && curMonth >= dto.getPaymentMonth()) {
			return true;
		}
		return false;
    }
    
    public static List<PaymentDetailsDTO> getDuesList(Long flatId, String flatNo, PaymentDetailsEntity lastPayment, LocalDate currentDate) {
    	List<PaymentDetailsDTO> duesList = new ArrayList<>();
    	if(lastPayment != null && currentDate != null) {
			
			Integer year = lastPayment.getPaymentYear();
			Integer month = lastPayment.getPaymentMonth()+1;
			
			Integer currentYear = currentDate.getYear();
			Integer currentMonth = currentDate.getMonth().getValue();
			
			PaymentDetailsDTO payDtlsDto = null;
			
			long tempRecId = 1;
			int tempfromMonth = month;
			int tempToMonth = 12;
			for(int yr = year; yr <= currentYear; yr++) {
				
				if(yr == currentYear) {
					tempToMonth = currentMonth;
				}
				for(int mn = tempfromMonth; mn <= tempToMonth; mn++) {
					payDtlsDto = new PaymentDetailsDTO();
					payDtlsDto.setId(tempRecId++);
					payDtlsDto.setFlatId(flatId);
					payDtlsDto.setFlatNo(flatNo);
					payDtlsDto.setPaymentMonth(mn);
					payDtlsDto.setPaymentMonthName(DateUtils.MONTH_NAME_MAP.getOrDefault(mn, ""));
					payDtlsDto.setPaymentYear(yr);
					duesList.add(payDtlsDto);
				}
				tempfromMonth = 1;
			}
			
		} else {
			throw new RuntimeException("Error on create due list.");
		}
		return duesList;
    }
    
    /*
     * Get last payment from Session Details while last payment not found
     */
    public static PaymentDetailsEntity validateLastPayment(PaymentDetailsEntity lastPayment, SessionDetailsEntity sessionDtls) {
    	if(sessionDtls != null) {
			if(lastPayment == null && sessionDtls.getFromDate() != null) {
				
				LocalDate localDate = sessionDtls.getFromDate().toInstant()
					      .atZone(ZoneId.systemDefault())
					      .toLocalDate();
				
				lastPayment = new PaymentDetailsEntity();
				lastPayment.setPaymentYear(localDate.getYear());
				lastPayment.setPaymentMonth(localDate.getMonth().getValue()-1);
				
			}
    	}
    	
    	return lastPayment;
    }
    
    public static LocalDate getCurrentDateFromSessionDetails(SessionDetailsEntity sessionDtls) {
    	LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
    	if(sessionDtls != null && sessionDtls.getToDate() != null) {
			LocalDate toDt = sessionDtls.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			//currentDate = currentDate.isAfter(toDt) ? toDt : currentDate; //currently paused
			currentDate = toDt;
		}
		return currentDate;
    }
    
    public static SessionDetailsEntity getCurrentSessionDetails(List<SessionDetailsEntity> sessionDtlsList) {
    	SessionDetailsEntity sessionDetails = null;
    	if(sessionDtlsList != null && !sessionDtlsList.isEmpty()) {
    		LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
    		sessionDetails = sessionDtlsList.stream().filter( f -> 
    		
	    		currentDate.isEqual(f.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
	    		|| currentDate.isEqual(f.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
	    		|| (currentDate.isAfter(f.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
	    				&& currentDate.isBefore(f.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()))
    		
    		).findFirst().orElse(null);
		}
		return sessionDetails;
    }
    
    
    public static List<String> getPermissionList(UserRolePermissionEntity entity) {
    	List<String> list = new ArrayList<>();
    	if(entity.getCreate() != null && entity.getCreate()) {
    		list.add(ApiConstants.USER_PERMISSION_CREATE);
    	}
    	
    	if(entity.getView() != null && entity.getView()) {
    		list.add(ApiConstants.USER_PERMISSION_VIEW);
    	}
    	
    	if(entity.getEdit() != null && entity.getEdit()) {
    		list.add(ApiConstants.USER_PERMISSION_EDIT);
    	}
    	
    	if(entity.getDelete() != null && entity.getDelete()) {
    		list.add(ApiConstants.USER_PERMISSION_DELETE);
    	}
    	
    	
    	return list;
    }
}
