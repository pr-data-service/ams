/**
 * 
 */
package com.drps.ams.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.drps.ams.dto.ListViewFieldDTO;
import com.drps.ams.dto.RequestParamDTO;

/**
 * @author 002ZX2744
 *
 */
public class DynamicQuery<T> implements Specification<T>{

	RequestParamDTO reqParamDto = null;
	
	public DynamicQuery(RequestParamDTO reqParamDto) {
		this.reqParamDto = reqParamDto;
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		
		List<Predicate> predicates = new ArrayList<>();
		addDefaultFilter(root, criteriaBuilder, predicates);
		
		addParentRecord(root, criteriaBuilder, predicates);
		addSearchFieldValue(root, criteriaBuilder, predicates);
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
	
	private void addDefaultFilter(Root<T> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		System.out.println("Entity Name: "+root.getModel().getName());		
		try {
			String fieldName = root.getModel().getAttribute("sessionId").getName();
			predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(fieldName), Utils.getUserContext().getSessionId())));
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
		try {
			String fieldName = root.getModel().getAttribute("apartmentId").getName();
			predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(fieldName), Utils.getUserContext().getApartmentId())));
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
	}
	
	private void addParentRecord(Root<T> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		if(reqParamDto != null && reqParamDto.getParentFieldName() != null && !reqParamDto.getParentFieldName().isBlank()
				&& reqParamDto.getParentRecordId() > 0) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(reqParamDto.getParentFieldName()), reqParamDto.getParentRecordId())));
		}
		
	}
	
	private void addSearchFieldValue(Root<T> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		if(reqParamDto != null && reqParamDto.getSearchFieldName() != null && !reqParamDto.getSearchFieldName().isBlank()) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get(reqParamDto.getSearchFieldName()), "%"+reqParamDto.getSearchFieldValue()+"%")));
		}
		
		if(reqParamDto != null && reqParamDto.getSeacrchFields() != null && !reqParamDto.getSeacrchFields().isEmpty()) {
			
			for(ListViewFieldDTO fieldDto: reqParamDto.getSeacrchFields()) {
				if(fieldDto != null) {
					if(DBConstants.FIELD_TYPE_TEXT.equalsIgnoreCase(fieldDto.getType())) {
						predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get(fieldDto.getDataField()), "%"+fieldDto.getValue()+"%")));
					} else {
						predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get(fieldDto.getDataField()), fieldDto.getValue())));
					}				
				}
			}
		}
		
	}
}
