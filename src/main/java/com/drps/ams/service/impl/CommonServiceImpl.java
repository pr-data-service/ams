package com.drps.ams.service.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drps.ams.bean.UserContext;
import com.drps.ams.dto.ApiResponseEntity;
import com.drps.ams.dto.EventsDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.LinkFlatDetailsAndUserDetailsEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.entity.UserDetailsEntity;
import com.drps.ams.exception.UserContextNotFoundException;
import com.drps.ams.repository.EventsRepository;
import com.drps.ams.repository.FlatDetailsRepository;
import com.drps.ams.repository.LinkFlatDetailsAndUserDetailsRepository;
import com.drps.ams.repository.PaymentDetailsRepository;
import com.drps.ams.repository.SessionDetailsRepository;
import com.drps.ams.repository.UserDetailsRepository;
import com.drps.ams.service.CommonService;
import com.drps.ams.util.ApiConstants;
import com.drps.ams.util.Utils;


@Service
public class CommonServiceImpl implements CommonService {

	private static final Logger logger = LogManager.getLogger(CommonServiceImpl.class);
	
	@Autowired
	LinkFlatDetailsAndUserDetailsRepository linkFlatDetailsAndUserDetailsRepository;
	
	@Autowired
	UserDetailsRepository userDetailsRepository;
	
	@Autowired
	FlatDetailsRepository flatDetailsRepository;
	
	@Autowired
	SessionDetailsRepository sessionDetailsRepository;
	
	@Autowired
	EventsRepository eventsRepository;
	
	@Transactional
	@Override
	public ApiResponseEntity saveLinkObject(RequestParamDTO reqParamDto) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		UserContext userContext = Utils.getUserContext();
		
		if(ApiConstants.OBJECT_FLAT_DETAILS.equalsIgnoreCase(reqParamDto.getObject())) {
			long id = Long.parseLong(String.valueOf(reqParamDto.getId()));
			LinkFlatDetailsAndUserDetailsEntity entityOld = linkFlatDetailsAndUserDetailsRepository.getActiveLinkObjectDetails(userContext.getApartmentId(), id);
			if(entityOld != null) {
				entityOld.setIsActive(false);
				entityOld.setUnlinkDate(new Date());
				entityOld.setModifiedBy(userContext.getUserId());
				linkFlatDetailsAndUserDetailsRepository.save(entityOld);
			}
			
			
			String strLinkTableName = "link_"+reqParamDto.getObject() + "_" + reqParamDto.getLinkObject();
			strLinkTableName = strLinkTableName.toLowerCase();
			if(ApiConstants.createInstByTableName(strLinkTableName).getClass() == LinkFlatDetailsAndUserDetailsEntity.class) {
				LinkFlatDetailsAndUserDetailsEntity obj = ApiConstants.createInstByTableName(strLinkTableName);
				obj.setFlatId(reqParamDto.getId());
				obj.setUserId(reqParamDto.getLinkRecordId());
				obj.setIsActive(true);
				obj.setLinkDate(new Date());
				obj.setCreatedBy(userContext.getUserId());
				obj.setModifiedBy(userContext.getUserId());
				obj.setApartmentId(userContext.getApartmentId());
				linkFlatDetailsAndUserDetailsRepository.save(obj);
			}
			
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, ApiConstants.STATUS_MESSAGE.get(ApiConstants.RESP_STATUS_SUCCESS));
	}
	
	@Override
	public ApiResponseEntity getLinkObjectDetails(String reqParams) {
		UserContext userContext = Utils.getUserContext();
		
		RequestParamDTO reqParamDto = RequestParamDTO.getInstance(reqParams);
		Map<String, Object> respMap = new HashMap<>();
		
		if(ApiConstants.OBJECT_FLAT_DETAILS.equalsIgnoreCase(reqParamDto.getObject())
				&& ApiConstants.OBJECT_USER_DETAILS.equalsIgnoreCase(reqParamDto.getLinkObject())) {
			String strLinkTableName = "link_"+reqParamDto.getObject() + "_" + reqParamDto.getLinkObject();
			strLinkTableName = strLinkTableName.toLowerCase();
			Class clss = ApiConstants.LINK_OBJECT_ENTITY_MAP.get(strLinkTableName);
			if(clss != null) {
				List<Object[]> list = linkFlatDetailsAndUserDetailsRepository.getLinkObjectDetails(userContext.getApartmentId(), Long.valueOf(reqParamDto.getId()));
				System.out.println(list);
				List<UserDetailsEntity> userList = userDetailsRepository.getAll(userContext.getApartmentId());
				if(list != null && !list.isEmpty()) {
					Object arr[] = list.get(0);
					respMap.put("id", arr[0]);
					respMap.put("flatId", arr[1]);
					respMap.put("userId", arr[2]);
					respMap.put("firstName", arr[3]);
					respMap.put("lastName", arr[4]);
					respMap.put("contactNo1", arr[5]);
					
					
					if(userList != null && !userList.isEmpty()) {
						userList = userList.stream().filter( f -> f.getId() != Integer.parseInt(arr[2].toString()) ).collect(Collectors.toList());
					}					
				}
				respMap.put("userList", userList);
			}
			
		}
		return new ApiResponseEntity(ApiConstants.RESP_STATUS_SUCCESS, respMap);
	}
	
	@Override
	public <T> void addUserDetailsToDTO(List<T> list, Class<T> cls) {
		UserContext userContext = Utils.getUserContext();
		try {
			if(list != null) {
				String systemUserName = ApiConstants.getSystemUserName(ApiConstants.SYSTEM_USER_ID);
				
				List<UserDetailsEntity> userDetailsList = userDetailsRepository.getAll(userContext.getApartmentId());		
				Map<Long, UserDetailsEntity> flatListMap = userDetailsList.stream().collect(Collectors.toMap( x -> x.getId(), x -> x));
				
				for(T t : list) {
					if(t != null) {
						Field fldCreatedBy = t.getClass().getDeclaredField("createdBy");
						fldCreatedBy.setAccessible(true);
						Long createdBy = (Long)fldCreatedBy.get(t);
						fldCreatedBy.setAccessible(false);
						
						Field fldModifiedBy = t.getClass().getDeclaredField("modifiedBy");
						fldModifiedBy.setAccessible(true);
						Long modifiedBy = (Long)fldModifiedBy.get(t);
						fldModifiedBy.setAccessible(false);
						
						String createdByName = ApiConstants.SYSTEM_USER_ID == createdBy ? systemUserName : Utils.getUserFullName(flatListMap.get(createdBy));
						String modifiedByName = ApiConstants.SYSTEM_USER_ID == modifiedBy ? systemUserName : Utils.getUserFullName(flatListMap.get(modifiedBy));
						
						cls.getField("createdByName").set(t, createdByName);
						cls.getField("modifiedByName").set(t, modifiedByName);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}		
	}
	
	@Override
	public <T> void addUserDetailsToDTO(T t, Class<T> cls) {
		UserContext userContext = Utils.getUserContext();
		try {
			if(t != null) {
				
				List<UserDetailsEntity> userDetailsList = userDetailsRepository.getAll(userContext.getApartmentId());		
				Map<Long, UserDetailsEntity> flatListMap = userDetailsList.stream().collect(Collectors.toMap( x -> x.getId(), x -> x));
				
				Field fldCreatedBy = t.getClass().getDeclaredField("createdBy");
				fldCreatedBy.setAccessible(true);
				Long createdBy = (Long)fldCreatedBy.get(t);
				fldCreatedBy.setAccessible(false);
				
				Field fldModifiedBy = t.getClass().getDeclaredField("modifiedBy");
				fldModifiedBy.setAccessible(true);
				Long modifiedBy = (Long)fldModifiedBy.get(t);
				fldModifiedBy.setAccessible(false);
				
				cls.getField("createdByName").set(t, Utils.getUserFullName(flatListMap.get(createdBy)));
				cls.getField("modifiedByName").set(t, Utils.getUserFullName(flatListMap.get(modifiedBy)));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}		
	}
	
	@Override
	public <T> void addFlatNoToDTO(List<T> list) {
		UserContext userContext = Utils.getUserContext();
		try {
			if(list != null && !list.isEmpty() && list.get(0) != null) {
				Class cls = list.get(0).getClass();
				Field fldId = cls.getDeclaredField("flatId");
				fldId.setAccessible(true);
				List<Long> flatIds = list.stream().map( m -> {
					try {
						return (Long)fldId.get(m);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					}
					return null;
				}).distinct().toList();
				fldId.setAccessible(true);
				
				List<FlatDetailsEntity> flatList = flatDetailsRepository.getFlatList(userContext.getApartmentId(), flatIds);		
				Map<Long, String> flatListMap = flatList.stream().collect(Collectors.toMap(FlatDetailsEntity::getId, FlatDetailsEntity::getFlatNo));
				
				
				Field fldFlatNo = cls.getDeclaredField("flatNo");
				list.forEach( f -> {
					if(f != null) {
						try {
							fldFlatNo.set(f, (String)flatListMap.get((Long)fldId.get(f)));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}		
	}
	
	@Override
	public <T> void addSessionNameToDTO(List<T> list) {
		UserContext userContext = Utils.getUserContext();
		try {
			if(list != null && !list.isEmpty() && list.get(0) != null) {
				Class cls = list.get(0).getClass();
				Field fldSessionId = cls.getDeclaredField("sessionId");
				fldSessionId.setAccessible(true);
				List<Long> sessionIds = list.stream().map( m -> {
					try {
						return (Long)fldSessionId.get(m);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					}
					return null;
				}).distinct().toList();
				fldSessionId.setAccessible(true);
				
				List<SessionDetailsEntity> sessionList = sessionDetailsRepository.getSessionDetailsList(userContext.getApartmentId(), sessionIds);		
				Map<Long, String> sessionDetailsListMap = sessionList.stream().collect(Collectors.toMap(SessionDetailsEntity::getId, SessionDetailsEntity::getName));
				
				
				Field fldName = cls.getDeclaredField("sessionName");
				list.forEach( f -> {
					if(f != null) {
						try {
							fldName.set(f, (String)sessionDetailsListMap.get((Long)fldSessionId.get(f)));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}		
	}
	
	
	@Override
	public <T> void addEventNameToDTO(List<T> list) {
		UserContext userContext = Utils.getUserContext();
		try {
			if(list != null && !list.isEmpty() && list.get(0) != null) {
				Class cls = list.get(0).getClass();
				Field fldId = cls.getDeclaredField("eventId");
				fldId.setAccessible(true);
				List<Long> ids = list.stream().map( m -> {
					try {
						return (Long)fldId.get(m);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					}
					return null;
				}).distinct().toList();
				fldId.setAccessible(true);
				
				List<EventsEntity> eventList = eventsRepository.getListByIds(userContext.getApartmentId(), ids);
				Map<Long, String> listMap = eventList.stream().collect(Collectors.toMap(EventsEntity::getId, EventsEntity::getName));
				
				
				Field fldName = cls.getDeclaredField("eventName");
				list.forEach( f -> {
					if(f != null) {
						try {
							fldName.set(f, (String)listMap.get((Long)fldId.get(f)));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}		
	}
	
	@Override
	public <T> void addPaymentByToDTO(List<T> list) {
		UserContext userContext = Utils.getUserContext();
		try {
			if(list != null && !list.isEmpty() && list.get(0) != null) {
				Class cls = list.get(0).getClass();
				Field paymentById = cls.getDeclaredField("paymentBy");
				paymentById.setAccessible(true);
				List<Long> paymentByIds = list.stream().map( m -> {
					try {
						return (Long)paymentById.get(m);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						logger.error(e.getMessage());
					}
					return null;
				}).distinct().toList();
				paymentById.setAccessible(true);
				
				List<UserDetailsEntity> userDetailsList = userDetailsRepository.getAll(userContext.getApartmentId());		
				Map<Long, UserDetailsEntity> userListMap = userDetailsList.stream().collect(Collectors.toMap( x -> x.getId(), x -> x));
				
				
				Field paymentByName = cls.getDeclaredField("paymentByName");
				list.forEach( f -> {
					if(f != null) {
						try {
							paymentByName.set(f, Utils.getUserFullName(userListMap.get((Long)paymentById.get(f))));
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							logger.error(e.getMessage());
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}		
	}
}
