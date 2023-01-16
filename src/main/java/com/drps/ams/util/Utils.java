package com.drps.ams.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.drps.ams.entity.UserDetailsEntity;
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

}
