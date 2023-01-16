package com.drps.ams.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSaveDTO {

	Long id;
	Long flatId;
	String paymentMode;
	String paymentModeRef;
	Date paymentDate;  
	String remarks;
	List<PaymentDetailsDTO> monthList;
//	Double maintenancePerMonth;
	List<MiscellaneousFieldDTO> miscellaneousFields;
}
