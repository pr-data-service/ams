package com.drps.ams.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
	
	private Double total;
	private Double cash;
	private Double online;
	private Double cheque;

}
