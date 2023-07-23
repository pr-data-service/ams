package com.drps.ams.util;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.entity.EventsEntity;

public class ExcelFiles {
	
	public static void paymentWithDetails(List<PaymentDTO> pdtos, List<PaymentDetailsDTO> ddtos, List<EventsEntity> eventList, String folderName,
			String fileName) throws Exception {

		XSSFWorkbook workBook = new XSSFWorkbook();
		
		//payment with details sheet..
		Sheet sheet = workBook.createSheet("audit-payment-" + folderName);
		ExcelFileUtils.paymentWithDetailsRows(sheet, pdtos, ddtos);

		//only for payment sheet
		Sheet sheet2 = workBook.createSheet("payment-" + folderName);
		List<String> headerFields = new ArrayList<>(Arrays.asList(new String[] { "Srl No", "Bill No", "Flat No", "Amount", "Payment Mode", "Payment Mode Ref",
				"Payment Date", "Payment By", "Is Canceled", "Cancel Remarks", "Created Date", "Created By" }));
		
		List<String> eventNames = eventList.stream().map(EventsEntity::getName).collect(Collectors.toList());
		headerFields.addAll(3, eventNames);
		
		ExcelFileUtils.paymentHeader(sheet2, headerFields);
		ExcelFileUtils.paymentRows(sheet2, pdtos, ddtos, eventList);

		FileOutputStream outputStream = new FileOutputStream(fileName + ".xlsx");
		workBook.write(outputStream);
		workBook.close();
	}
	/*
	public static void payment(List<PaymentDTO> pdtos, String sheetName, String fileName) throws Exception {
		
		List<String> headerFields = Arrays.asList(new String[] {"Srl No", "Bill No", "Flat Id", "Amount", "Payment Mode", "Payment Mode Ref", "Payment Date", "Payment By", "Is Canceled", "Cancel Remarks", "Created Date", "Created By"}) ; 
		XSSFWorkbook workBook = new XSSFWorkbook();
		Sheet sheet = workBook.createSheet(sheetName);
		ExcelFileUtils.paymentHeader(sheet, headerFields);
		ExcelFileUtils.paymentRows(sheet, pdtos);
        FileOutputStream outputStream = new FileOutputStream(fileName+".xlsx");
        workBook.write(outputStream);
        workBook.close();
	}
	*/
	public static void expensesWithItems(List<ExpensesDTO> edtos, List<ExpenseItemsDTO> idtos, String folderName,
			String fileName) throws Exception {

		XSSFWorkbook workBook = new XSSFWorkbook();
		
		Sheet sheet = workBook.createSheet("audit-expense-" + folderName);
		ExcelFileUtils.expenseWithItemsRows(sheet, edtos, idtos);

		Sheet sheet2 = workBook.createSheet("expense-" + folderName);
		List<String> headerFields1 = Arrays
				.asList(new String[] { "Srl No", "Voucher No", "Title", "Amount", "Payment Mode", "Description",
						"Expense Date", "EventName", "Is Canceled", "Cancel Remarks", "Created Date", "Created By" });
		ExcelFileUtils.expenseHeader(sheet2, headerFields1);
		ExcelFileUtils.expenseRows(sheet2, edtos);

		FileOutputStream outputStream = new FileOutputStream(fileName + ".xlsx");
		workBook.write(outputStream);
		workBook.close();
	}
}
