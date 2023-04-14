package com.drps.ams.util;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.entity.PaymentEntity;

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
		for(PaymentDTO entity: rows) {
			Row row = sheet.createRow(rowNum++);
			Cell cell = row.createCell(0);
			cell.setCellValue(rowNum-1);
			
			cell = row.createCell(1);
			cell.setCellValue(entity.getBillNo() == null ? "" : entity.getBillNo());
			
			cell = row.createCell(2);
			cell.setCellValue(entity.getFlatNo() == null ? "" : entity.getFlatNo());
			
			cell = row.createCell(3);
			cell.setCellValue(entity.getAmount() == null ? 0.00 : entity.getAmount());
			
			cell = row.createCell(4);
			cell.setCellValue(entity.getPaymentMode() == null ? "" : entity.getPaymentMode());
			
			cell = row.createCell(5);
			cell.setCellValue(entity.getPaymentModeRef() == null ? "" : entity.getPaymentModeRef());
			
			cell = row.createCell(6);
			cell.setCellValue(entity.getPaymentDate() == null ? DateUtils.stringToDate("00-00-0000") : entity.getPaymentDate());
			
			cell = row.createCell(7);
			cell.setCellValue(entity.getPaymentByName() == null ? "" : entity.getPaymentByName());
			
			cell = row.createCell(8);
			cell.setCellValue(entity.getIsCanceled() == null ? false : entity.getIsCanceled());
			
			cell = row.createCell(9);
			cell.setCellValue(entity.getCancelRemarks() == null ? "" : entity.getCancelRemarks());
			
			cell = row.createCell(10);
			cell.setCellValue(entity.getCreatedDate() == null ? DateUtils.stringToDate("00-00-0000") : entity.getCreatedDate());
			
			cell = row.createCell(11);
			cell.setCellValue(entity.getCreatedByName() == null ? "" : entity.getCreatedByName());
			
		}
	}
	
	public static void header(Sheet sheet, List<Object> fields) {
		Row row = sheet.createRow(0);
	
		for (int i = 0; i < fields.size(); i++) { 
			Cell cell = row.createCell(i);
			cell.setCellValue((String)fields.get(i));
		}
	}
}
