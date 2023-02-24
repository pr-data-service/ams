package com.drps.ams.dbquery;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.el.util.ReflectionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.drps.ams.annotation.EntityFieldMapping;
import com.drps.ams.annotation.FKEntityFieldMapping;
import com.drps.ams.dto.ListViewFieldDTO;
import com.drps.ams.dto.QueryOrderByDTO;
import com.drps.ams.dto.RequestParamDTO;
import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.ExpensesEntity;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.MaintenanceEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.entity.SessionDetailsEntity;
import com.drps.ams.util.DBConstants;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.ParameterVerifier;
import com.drps.ams.util.ReflectionUtils;
import com.drps.ams.util.Utils;

public class QueryMaker<T> {
	
	RequestParamDTO reqParamDto = null;
	private Class<T> rtnCls;
	private Class root;
	private Map<String, FKEntityFieldMapping> fkEntityFieldMap;
	
	public QueryMaker(RequestParamDTO reqParamDto, Class<T> rtnCls) {
		this.reqParamDto = reqParamDto;
		this.rtnCls = rtnCls;
		
		this.root = ReflectionUtils.getRootEntity(rtnCls);
		this.fkEntityFieldMap = ReflectionUtils.getFKEntityFieldMappingsMap(rtnCls);
	}
	
	private String getSelectQry() {
		StringBuilder sb = new StringBuilder("SELECT");
		
		List<Class<?>> entityList = new ArrayList<>();
		entityList.add(root);
		if(fkEntityFieldMap != null) {
			entityList.addAll(fkEntityFieldMap.entrySet().stream().map( m -> m.getValue().entity()).collect(Collectors.toList()));	
		}
				
		String selectQry = entityList.stream().map( m -> Utils.wordCamelCase(m.getSimpleName())).collect(Collectors.joining(","));
		sb.append(" ").append( selectQry );
		return sb.toString();
	}
	
	private String getFromQuery() {
		StringBuilder sb = new StringBuilder("FROM");
		sb.append(" ").append(root.getSimpleName()).append( " ").append(Utils.wordCamelCase(root.getSimpleName()));
		
		if(fkEntityFieldMap != null && fkEntityFieldMap.size() > 0) {
			//INNER JOIN EventsEntity eventsEntity ON paymentDetailsEntity.eventId = eventsEntity.id
			String joinQry = "";
			FKEntityFieldMapping fk = null;
			for(String fldNm : fkEntityFieldMap.keySet()) {
				fk = fkEntityFieldMap.get(fldNm);
				joinQry = fk.join() + " " 
						+ fk.entity().getSimpleName() + " "+ Utils.wordCamelCase(fk.entity().getSimpleName())
						+ " ON "
						+ Utils.wordCamelCase(root.getSimpleName()) + "." + fldNm 
							+ " = " + Utils.wordCamelCase(fk.entity().getSimpleName()) + ".id";				
				sb.append(" ").append(joinQry);
			}
		}
		System.out.println("joinQry: " + sb);
		return sb.toString();
	}

	public String getQuery() {
		
		StringBuilder sb = new StringBuilder();
		String selectQry = getSelectQry();
		String fromQry = getFromQuery();
		
		sb.append(selectQry);
		sb.append(" ").append(fromQry);
		
		String defaultFilter = getDefaultFilter();
		String parentRecordClause = getParentRecordClause();
		String filterClause = getFilterClause();
		String orderByClause = getOrderBy();
		
		String whereClause = "";		
		if(!defaultFilter.isEmpty()) {
			whereClause += whereClause.isEmpty() ? defaultFilter : " AND " + defaultFilter;
		}
		
		if(!parentRecordClause.isEmpty()) {
			whereClause += whereClause.isEmpty() ? parentRecordClause : " AND " + parentRecordClause;
		}
		
		if(!filterClause.isEmpty()) {
			whereClause += whereClause.isEmpty() ? filterClause : " AND " + filterClause;
		}
		
		if(!whereClause.isEmpty()) {
			sb.append( " WHERE " ).append(whereClause);
		}	
		
		if(!orderByClause.isEmpty()) {
			sb.append( " ORDER BY ").append(orderByClause);
		}
		return sb.toString();
	}
		
	private String getDefaultFilter() {
		String rootEntity = Utils.wordCamelCase(root.getSimpleName());
		String clause = "";
		try {
			if(root.getDeclaredField("sessionId") != null) {
				clause += rootEntity+".sessionId = "+ Utils.getUserContext().getSessionId();
			}
		} catch (Exception e) {}
		
		try {
			if(root.getDeclaredField("apartmentId") != null) {
				if(!clause.isEmpty()) {
					clause += " AND ";
				}
				clause += rootEntity+".apartmentId = "+ Utils.getUserContext().getApartmentId();
			}
		} catch (Exception e) {}
		return clause;
	}
	
	private String getParentRecordClause() {
		String clause = "";
		if(reqParamDto != null && !ParameterVerifier.isBlank(reqParamDto.getParentObject())) {
			String rootEntity = Utils.wordCamelCase(root.getSimpleName());
			clause += rootEntity+".parentObject = '"+ reqParamDto.getParentObject()+"'";
		}
		
		if(reqParamDto != null && !ParameterVerifier.isBlank(reqParamDto.getParentFieldName())
				&& reqParamDto.getParentRecordId() > 0) {
			String rootEntity = Utils.wordCamelCase(root.getSimpleName());
			if(!clause.isEmpty()) {
				clause +=" AND ";
			}
			clause += rootEntity+"." + reqParamDto.getParentFieldName() + " = "+ reqParamDto.getParentRecordId();
		}
		return clause;
	}
	
	private String getFilterClause() {
		String clause = "";
		if(reqParamDto != null && reqParamDto.getSeacrchFields() != null && !reqParamDto.getSeacrchFields().isEmpty()) {
			List<String> clauseList = new ArrayList<>();
			for(ListViewFieldDTO fieldDto: reqParamDto.getSeacrchFields()) {
				if(fieldDto != null) {
					clauseList.add(getFieldClause(fieldDto));
				}
			}
			clause = clauseList.stream().filter( f -> !f.isEmpty()).collect(Collectors.joining(" AND "));
		}
		return clause;
	}
	
	public String getOrderBy() {
		String clause = "";
		
		List<QueryOrderByDTO> list = reqParamDto.getOrderByFields();
		if(list != null && !list.isEmpty()) {
			String rootEntity = Utils.wordCamelCase(root.getSimpleName());
			for(QueryOrderByDTO dto : list) {
				if(dto != null && dto.getFieldName() != null && !dto.getFieldName().trim().isEmpty()) {
					clause += clause.isEmpty() ? "" : ", ";
					clause += rootEntity + "." + dto.getFieldName() + " " + (dto.isAsc() ? "ASC" : "DESC");
				}				
			}
		}
		return clause;
	}
	
	public void setParams(Query query) {
		if(reqParamDto != null && reqParamDto.getSeacrchFields() != null && !reqParamDto.getSeacrchFields().isEmpty()) {
			for(ListViewFieldDTO fieldDto: reqParamDto.getSeacrchFields()) {
				if(fieldDto != null && fieldDto.getDataField() != null && !fieldDto.getDataField().trim().isEmpty() && fieldDto.getValue() != null && !fieldDto.getValue().toString().trim().isEmpty()) {
					if(DBConstants.FIELD_TYPE_DATE.equalsIgnoreCase(fieldDto.getType())) {
						String arr[] = fieldDto.getValue().toString().split("@@##");
						query.setParameter("from"+fieldDto.getDataField(), DateUtils.stringToDateTime(arr[0]));
						query.setParameter("to"+fieldDto.getDataField(), DateUtils.stringToDateTime(arr[1]));
					} else {
						query.setParameter(fieldDto.getDataField(), fieldDto.getValue());
					}
				}
			}
		}
	}
	
	private String getFieldClause(ListViewFieldDTO fieldDto) {
		String clause = "";
		try {
			if(fieldDto.getValue() != null && !fieldDto.getValue().toString().trim().isEmpty()) {
				String fieldName = fieldDto.getDataField();
				
				Class cls = ReflectionUtils.isFieldNameExist(root, fieldDto.getDataField()) ? root : null;
				if(cls == null) {
					EntityFieldMapping fkFieldMappingValue = ReflectionUtils.getFKFieldMappingValueAnnotation(rtnCls, fieldName, EntityFieldMapping.class);
					cls = fkFieldMappingValue.entity();
					fieldName = fkFieldMappingValue.name();
				}
				
				if(cls != null) {
					if(DBConstants.FIELD_TYPE_TEXT.equalsIgnoreCase(fieldDto.getType())) {
						clause = Utils.wordCamelCase(cls.getSimpleName()) + "."+fieldName + " LIKE CONCAT('%', :" + fieldDto.getDataField() + ", '%')";
					} else if(DBConstants.FIELD_TYPE_DATE.equalsIgnoreCase(fieldDto.getType())) {
						clause = Utils.wordCamelCase(cls.getSimpleName()) + "."+fieldName + " BETWEEN :from" + fieldDto.getDataField() + " AND :to" + fieldDto.getDataField();
					} else {
						clause = Utils.wordCamelCase(cls.getSimpleName()) + "."+fieldName + " = :" + fieldDto.getDataField();
						ReflectionUtils.castValue(cls, fieldDto);
					}
				}			
			}
		} catch (NullPointerException e) {
//			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return clause;
	}
	
	public List<T> convertData(List<Object[]> list) {
		List<T> rtnList = new ArrayList<>();
		for(Object listObj : list) {
			try {
				T t = rtnCls.newInstance();
				
				if(listObj.getClass().isArray()) {
					int count = 1;
					for(Object obj: (Object[])listObj) {
						if(obj != null) {
							if(count == 1 ) {
								BeanUtils.copyProperties(obj, t);
							} else {
								BeanUtils.copyProperties(obj, t, Utils.getIgnoreEntityPropsOnUpdate(new String[] {"id"}));
								ReflectionUtils.setFieldValue(obj, t);
							}
						}
						count++;
					}
				} else {
					BeanUtils.copyProperties(listObj, t);
				}
				rtnList.add(t);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return rtnList;
	}
	
	
	
	public static void main(String ...args) {
		
		
		
//		System.out.println(QueryMaker.class.getConstructors()[0].getParameters()[0].getName());
//		System.out.println(QueryMaker.class.getCanonicalName());
		
//		for(Parameter par: QueryMaker.class.getConstructors()[0].getParameters()) {
//			System.out.println(	par.getName());
//		}
		
//		new QueryMaker<>(null)
	}
}
