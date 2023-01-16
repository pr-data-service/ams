package com.drps.ams.dbquery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.entity.FlatDetailsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.util.Utils;

@Repository
public class DBQueryExecuter {

	@PersistenceContext
	private EntityManager entityManager;
	
	public <T> List<T> executeQuery(QueryMaker queryMaker) {
		
		Query query = entityManager.createQuery(queryMaker.getQuery());
		queryMaker.setParams(query);
		List<Object[]> queryRows = query.getResultList();
		
		List<T> list = queryMaker.convertData(queryRows);
		
		
		return list;		
	}
	
	
	
	
	
	
	
	
}
