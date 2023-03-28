package com.drps.ams.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.RequestParamDTO;

public interface CommonService {

	ApiResponseEntity saveLinkObject(RequestParamDTO reqParamDto) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException ;

	ApiResponseEntity getLinkObjectDetails(String reqParams);

	<T> void addUserDetailsToDTO(List<T> list, Class<T> cls);

	<T> void addUserDetailsToDTO(T t, Class<T> cls);

	<T> void addFlatNoToDTO(List<T> list);

	<T> void addSessionNameToDTO(List<T> list);

	<T> void addEventNameToDTO(List<T> list);

	<T> void addPaymentByToDTO(List<T> list);

	<T> void addOwnersNameAndContactNoToDTO(List<T> list);

}
