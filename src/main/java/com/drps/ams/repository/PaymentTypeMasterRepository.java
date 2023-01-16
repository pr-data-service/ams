package com.drps.ams.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.drps.ams.entity.PaymentTypeMasterEntity;

@Repository
public interface PaymentTypeMasterRepository extends JpaRepository<PaymentTypeMasterEntity, Long>, 
													CrudRepository<PaymentTypeMasterEntity, Long>,
													JpaSpecificationExecutor<PaymentTypeMasterEntity> {

}
