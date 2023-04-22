package com.drps.ams.util;

import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.drps.ams.dto.PaymentDTO;

public class ExcelFileUtils {
	
	//create excel sheet.....
	public static void createExcelSheet(List<Object> headerFields, List<PaymentDTO> rows, String sheetName, String fileName) throws Exception {
		Workbook workBook = new XSSFWorkbook();
		Sheet sheet = workBook.createSheet(sheetName);
		header(sheet, headerFields);
		rows(sheet, rows);
        FileOutputStream outputStream = new FileOutputStream(fileName+".xlsx");
        workBook.write(outputStream);
        workBook.close();
	}
	
	public static void rows(Sheet sheet, List<PaymentDTO> rows) {

		int rowNum = 1;
		for(PaymentDTO dto: rows) {
			Row row = sheet.createRow(rowNum++);
			Cell cell = row.createCell(0);
			cell.setCellValue(rowNum-1);
			
			cell = row.createCell(1);
			cell.setCellValue(dto.getBillNo() == null ? "" : dto.getBillNo());
			
			cell = row.createCell(2);
			cell.setCellValue(dto.getFlatNo() == null ? "" : dto.getFlatNo());
			
			cell = row.createCell(3);
			cell.setCellValue(dto.getAmount() == null ? 0.00 : dto.getAmount());
			
			cell = row.createCell(4);
			cell.setCellValue(dto.getPaymentMode() == null ? "" : dto.getPaymentMode());
			
			cell = row.createCell(5);
			cell.setCellValue(dto.getPaymentModeRef() == null ? "" : dto.getPaymentModeRef());
			
			cell = row.createCell(6);
			cell.setCellValue(dto.getPaymentDate() == null ? "" : DateUtils.dateToString(dto.getPaymentDate()));
			
			cell = row.createCell(7);
			cell.setCellValue(dto.getPaymentByName() == null ? "" : dto.getPaymentByName());
			
			cell = row.createCell(8);
			cell.setCellValue(dto.getIsCanceled() == null ? false : dto.getIsCanceled());
			
			
			cell = row.createCell(9);
			cell.setCellValue(dto.getCancelRemarks() == null ? "" : dto.getCancelRemarks());
			
			cell = row.createCell(10);
			cell.setCellValue(dto.getCreatedDate() == null ? "" : DateUtils.dateToString(dto.getCreatedDate()));
			
			cell = row.createCell(11);
			cell.setCellValue(dto.getCreatedByName() == null ? "" : dto.getCreatedByName());
			
		}
		
		Double totalAmount = rows.stream()
                .filter(dto -> dto.getIsCanceled() == null || dto.getIsCanceled() == false)
                .mapToDouble(PaymentDTO::getAmount)
                .sum();

		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(2);
		cell.setCellValue("Total Amount");
		
		cell = row.createCell(3);
		cell.setCellValue(totalAmount);
	}
	
	public static void header(Sheet sheet, List<Object> fields) {
		Row row = sheet.createRow(0);
	
		for (int i = 0; i < fields.size(); i++) { 
			Cell cell = row.createCell(i);
			cell.setCellValue((String)fields.get(i));
		}
	}	
}