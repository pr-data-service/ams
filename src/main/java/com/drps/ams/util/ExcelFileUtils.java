package com.drps.ams.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.drps.ams.dto.ExpenseItemsDTO;
import com.drps.ams.dto.ExpensesDTO;
import com.drps.ams.dto.PaymentDTO;
import com.drps.ams.dto.PaymentDetailsDTO;
import com.drps.ams.entity.EventsEntity;

public class ExcelFileUtils {
	
	public static void paymentRows(Sheet sheet, List<PaymentDTO> pdtos, List<PaymentDetailsDTO> ddtos, List<EventsEntity> eventList) {
		int rowNum = 1;
		int colNm = 0;
		for(PaymentDTO entity: pdtos) {
			colNm = 0;
			Row row = sheet.createRow(rowNum++);
			Cell cell = row.createCell(colNm++);
			cell.setCellValue(rowNum-1);
			
			cell = row.createCell(colNm++);
			if(entity.getBillNo() != null) {
				cell.setCellValue(entity.getBillNo());
			}
			
			cell = row.createCell(colNm++);
			if(entity.getFlatNo() != null) {
				cell.setCellValue(entity.getFlatNo());
			}
			
			for(Long eventId : eventList.stream().map(EventsEntity::getId).collect(Collectors.toList())) {
				Optional<PaymentDetailsDTO> eventDtls = ddtos.stream().filter( f -> f.getPaymentId().equals(entity.getId())
						&& f.getEventId().equals(eventId)).findFirst();
				cell = row.createCell(colNm++);
				if(eventDtls.isPresent() && !Objects.isNull(eventDtls.get().getAmount())) {
					cell.setCellValue(eventDtls.get().getAmount());
				} else {
					cell.setCellValue(0);
				}
			}
			
			cell = row.createCell(colNm++);
			if(entity.getAmount() != null) {
				cell.setCellValue(entity.getAmount());
			}
			
			cell = row.createCell(colNm++);
			if(entity.getPaymentMode() != null) {
				cell.setCellValue(entity.getPaymentMode());
			}
			cell = row.createCell(colNm++);
			if(entity.getPaymentModeRef() != null) {
				cell.setCellValue(entity.getPaymentModeRef());
			}
			
			cell = row.createCell(colNm++);
			if(entity.getPaymentDate() != null) {
				cell.setCellValue(DateUtils.dateToString(entity.getPaymentDate()));
			}
			
			cell = row.createCell(colNm++);
			if(entity.getPaymentByName() != null) {
				cell.setCellValue(entity.getPaymentByName());
			}
			
			cell = row.createCell(colNm++);
			if(entity.getIsCanceled() != null) {
				cell.setCellValue(entity.getIsCanceled().booleanValue());
			}
			
			cell = row.createCell(colNm++);
			if(entity.getCancelRemarks() != null) {
				cell.setCellValue(entity.getCancelRemarks());
			}
			
			cell = row.createCell(colNm++);
			if(entity.getCreatedDate() != null) {
				cell.setCellValue(DateUtils.dateToString(entity.getCreatedDate()));
			}
			
			cell = row.createCell(colNm++);
			if(entity.getCreatedByName() != null) {
				cell.setCellValue(entity.getCreatedByName());
			}
		}
		
		Double totalPaymentAmount = pdtos.stream()
                .filter(dto -> dto.getIsCanceled() == null || dto.getIsCanceled().booleanValue() == false)
                .mapToDouble(PaymentDTO::getAmount)
                .sum();
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(2);
		cell.setCellValue("Total Amount");
		
		cell = row.createCell(3);
		if(totalPaymentAmount != null) {
			cell.setCellValue(totalPaymentAmount);
		}
	}
	
	public static void paymentWithDetailsRows(Sheet sheet, List<PaymentDTO> pdtos, List<PaymentDetailsDTO> ddtos) {

		XSSFCellStyle style = getStyleForHeader((XSSFWorkbook)sheet.getWorkbook());
		ExcelFileUtils.paymentWithDetailsHeader(sheet); // Object headings
		List<String> paymentFields = Arrays.asList(
				new String[] { "Bill No", "Flat No", "Payment Date", "Is Canceled", "Cancel Remarks", "Amount" });
		
		Double totalPaymentAmount = pdtos.stream()
                .filter(dto -> dto.getIsCanceled() == null || dto.getIsCanceled().booleanValue() == false)
                .mapToDouble(PaymentDTO::getAmount)
                .sum();
		
		int rowNum = 1;
		int srlNo = 0;

		for (PaymentDTO pdto : pdtos) {
			int length = 0;
			List<PaymentDetailsDTO> selDdtos = new ArrayList<PaymentDetailsDTO>();

			/// select paymentDetails using PaymentId
			for (PaymentDetailsDTO ddto : ddtos) {
				if (ddto.getPaymentId() == pdto.getId().longValue()) {
					selDdtos.add(ddto);
				}
			}

			/// Rows count for a complete payment and payment details
			if (paymentFields.size() > selDdtos.size() + 1) {
				length = paymentFields.size() + 1;
			} else {
				length = selDdtos.size() + 2;
			}
			

			// creating rows
			for (int i = 0; i < length; i++) {

				Row row = sheet.createRow(rowNum++);
				Cell cell = row.createCell(1);
				if(i < paymentFields.size()) {
					cell.setCellValue((String) paymentFields.get(i)); ///////// payment headings
					cell.setCellStyle(getStyleForHeader((XSSFWorkbook)sheet.getWorkbook()));
				}

				if (i == 0) {
					cell = row.createCell(0);
					cell.setCellValue("Serial No"); // serial number heading
					cell.setCellStyle(style);

					cell = row.createCell(2);
					if(pdto.getBillNo() != null) {
						cell.setCellValue(pdto.getBillNo());
					}
					paymentDetailsHeader(row); //// paymentDetailsHeading
				} else if (i == 1) {
					cell = row.createCell(0);
					cell.setCellValue(++srlNo); /// serial number

					cell = row.createCell(2);
					if(pdto.getFlatNo() != null) {
						cell.setCellValue(pdto.getFlatNo());
					}
				} else if (i == 2) {
					cell = row.createCell(2);
					if(pdto.getPaymentDate() != null) {
						cell.setCellValue(DateUtils.dateToStringForDB(pdto.getPaymentDate()));
					}
				} else if (i == 3) {
					cell = row.createCell(2);
					if(pdto.getIsCanceled() != null) {
						cell.setCellValue(pdto.getIsCanceled().booleanValue());
					}
				} else if (i == 4) {
					cell = row.createCell(2);
					if(pdto.getCancelRemarks() != null) {
						cell.setCellValue(pdto.getCancelRemarks());
					}
				} else if (i == 5) {
					cell = row.createCell(2);
					if(pdto.getAmount() != null) {
						cell.setCellValue(pdto.getAmount());
					}
				} 
				else if (i == (length-1)) {
					cell = row.createCell(7);
					cell.setCellValue("Total");
					
					cell = row.createCell(8);
					cell.setCellValue(selDdtos.stream()
							.mapToDouble(PaymentDetailsDTO::getAmount)
							.sum());
				}

				//// payment details data rows
				if (i > 0 && i <= selDdtos.size()) {

					cell = row.createCell(4);
					cell.setCellValue(i);
					
					cell = row.createCell(5);
					if(selDdtos.get(i-1).getEventName() != null) {
						cell.setCellValue(selDdtos.get(i-1).getEventName());
					}
					cell = row.createCell(6);
					if(selDdtos.get(i-1).getPaymentMonthName() != null) {
						cell.setCellValue(selDdtos.get(i-1).getPaymentMonthName());
					}

					cell = row.createCell(7);
					if(selDdtos.get(i-1).getPaymentYear() != null) {
						cell.setCellValue(selDdtos.get(i-1).getPaymentYear());
					}
					cell = row.createCell(8);
					if(selDdtos.get(i-1).getAmount() != null) {
						cell.setCellValue(selDdtos.get(i-1).getAmount());
					}
				}

			}
			rowNum = rowNum + 2;
		}
		Row row = sheet.createRow(rowNum++);
		
		Cell cell = row.createCell(1);
		cell.setCellValue("Total Payment Amount");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		if(totalPaymentAmount != null) {
			cell.setCellValue(totalPaymentAmount);
		}
	}
	
	public static void expenseRows(Sheet sheet, List<ExpensesDTO> edtos) {
		XSSFCellStyle style = getStyleForHeader((XSSFWorkbook)sheet.getWorkbook());
		int rowNum = 1;
		for(ExpensesDTO entity: edtos) {
			Row row = sheet.createRow(rowNum++);
			Cell cell = row.createCell(0);
			cell.setCellValue(rowNum-1);
			
			cell = row.createCell(1);
			if(entity.getVoucherNo() != null) {
				cell.setCellValue(entity.getVoucherNo());
			}
			
			cell = row.createCell(2);
			if(entity.getTitle() != null) {
				cell.setCellValue(entity.getTitle());
			}
			
			cell = row.createCell(3);
			if(entity.getAmount() != null) {
				cell.setCellValue(entity.getAmount());
			}
			
			cell = row.createCell(4);
			if(entity.getPaymentMode() != null) {
				cell.setCellValue(entity.getPaymentMode());
			}
			
			cell = row.createCell(5);
			if(entity.getDescription() != null) {
				cell.setCellValue(entity.getDescription());
			}
			
			cell = row.createCell(6);
			if(entity.getExpenseDate() != null) {
				cell.setCellValue(DateUtils.dateToString(entity.getExpenseDate()));
			}
			
			cell = row.createCell(7);
			if(entity.getEventName() != null) {
				cell.setCellValue(entity.getEventName());
			}
			
			cell = row.createCell(8);
			if(entity.getIsCanceled() != null) {
				cell.setCellValue(entity.getIsCanceled().booleanValue());
			}
			
			cell = row.createCell(9);
			if(entity.getCancelRemarks() != null) {
				cell.setCellValue(entity.getCancelRemarks());
			}
			
			cell = row.createCell(10);
			if(entity.getCreatedDate() != null) {
				cell.setCellValue(DateUtils.dateToString(entity.getCreatedDate()));
			}
			
			cell = row.createCell(11);
			if(entity.getCreatedByName() != null) {
				cell.setCellValue(entity.getCreatedByName());
			}
		}
		
		Double totalExpenseAmount = edtos.stream()
                .filter(dto -> dto.getIsCanceled() == null || dto.getIsCanceled().booleanValue() == false)
                .mapToDouble(ExpensesDTO::getAmount)
                .sum();
		Row row = sheet.createRow(rowNum++);
		Cell cell = row.createCell(2);
		cell.setCellValue("Total Amount");
		cell.setCellStyle(style);
		
		cell = row.createCell(3);
		if(totalExpenseAmount != null) {
			cell.setCellValue(totalExpenseAmount);
		}
	}
	
	public static void expenseWithItemsRows(Sheet sheet, List<ExpensesDTO> edtos, List<ExpenseItemsDTO> idtos) {
		
		XSSFCellStyle style = getStyleForHeader((XSSFWorkbook)sheet.getWorkbook());
		ExcelFileUtils.expenseWithItemsHeader(sheet); // Object headings
		List<String> expenseFields = Arrays.asList(
				new String[] {"Voucher No", "Title", "Expense Date", "Event Name", "Is Canceled", "Cancel Remarks", "Amount" });
		/// calculate total expense amount
		Double totalExpenseAmount = edtos.stream().filter(dto -> dto.getIsCanceled() == null || dto.getIsCanceled() == false)
				.mapToDouble(ExpensesDTO::getAmount).sum();
		
		int rowNum = 1;
		int srlNo = 0;
		
		for (ExpensesDTO edto : edtos) {
			int length = 0;
			List<ExpenseItemsDTO> selIdtos = new ArrayList<ExpenseItemsDTO>();

			/// select expenseItems using ExpenseId
			for (ExpenseItemsDTO Idto : idtos) {
				if (Idto.getExpenseId() == edto.getId().longValue()) {
					selIdtos.add(Idto);
				}
			}

			/// Rows count for a complete expense and expense items
			if (expenseFields.size() > selIdtos.size() + 1) {
				length = expenseFields.size() + 1;
			} else {
				length = selIdtos.size() + 2;
			}
			

			// creating rows
			for (int i = 0; i < length; i++) {

				Row row = sheet.createRow(rowNum++);
				Cell cell = row.createCell(1);
				if(i < expenseFields.size()) {
					cell.setCellValue((String) expenseFields.get(i)); ///////// expense headings
					cell.setCellStyle(style);
				}

				if (i == 0) {
					cell = row.createCell(0);
					cell.setCellValue("Serial No"); // serial number heading
					cell.setCellStyle(style);

					cell = row.createCell(2);
					if(edto.getVoucherNo() != null) {
						cell.setCellValue(edto.getVoucherNo());
					}
					expenseItemsHeader(row); //// expenseItemsHeading
				} else if (i == 1) {
					cell = row.createCell(0);
					cell.setCellValue(++srlNo); /// serial number

					cell = row.createCell(2);
					if(edto.getTitle() != null) {
						cell.setCellValue(edto.getTitle());
					}
				} else if (i == 2) {
					cell = row.createCell(2);
					if(edto.getExpenseDate() != null) {
						cell.setCellValue(DateUtils.dateToStringForDB(edto.getExpenseDate()));
					}
				} else if (i == 3) {
					cell = row.createCell(2);
					if(edto.getEventName() != null) {
						cell.setCellValue(edto.getEventName());
					}
				} else if (i == 4) {
					cell = row.createCell(2);
					if(edto.getIsCanceled() != null) {
						cell.setCellValue(edto.getIsCanceled().booleanValue());
					}
				} else if (i == 5) {
					cell = row.createCell(2);
					if(edto.getCancelRemarks() != null) {
						cell.setCellValue(edto.getCancelRemarks());
					}
				} else if (i == 6) {
					cell = row.createCell(2);
					if(edto.getAmount() != null) {
						cell.setCellValue(edto.getAmount());
					}
				} 
				else if (i == (length-1)) {
					cell = row.createCell(5);
					cell.setCellValue("Total");
					
					cell = row.createCell(6);
					cell.setCellValue(selIdtos.stream()
							.mapToDouble(ExpenseItemsDTO::getAmount)
							.sum());
				}

				//// expense items data rows
				if (i > 0 && i <= selIdtos.size()) {
					int j = i - 1;
					
					cell = row.createCell(4);
					cell.setCellValue(i);
					
					cell = row.createCell(5);
					if(selIdtos.get(j).getItemHead() != null) {
						cell.setCellValue(selIdtos.get(j).getItemHead());
					}
					
					cell = row.createCell(6);
					if(selIdtos.get(j).getAmount() != null) {
						cell.setCellValue(selIdtos.get(j).getAmount());
					}
				}

			}
			rowNum = rowNum + 2;
		}
		Row row = sheet.createRow(rowNum++);
		
		Cell cell = row.createCell(1);
		cell.setCellValue("Total Expense Amount");
		cell.setCellStyle(style);
		
		cell = row.createCell(2);
		if(totalExpenseAmount != null) {
			cell.setCellValue(totalExpenseAmount);
		}
	}
	
	
	///payment related headers
	public static void paymentHeader(Sheet sheet, List<String> fields) {
		XSSFCellStyle style = getStyleForHeader((XSSFWorkbook)sheet.getWorkbook());
		Row row = sheet.createRow(0);
	
		for (int i = 0; i < fields.size(); i++) { 
			Cell cell = row.createCell(i);
			cell.setCellValue(fields.get(i));
			cell.setCellStyle(style);
		}
	}
	
	public static XSSFCellStyle getStyleForHeader(XSSFWorkbook workbook) {
		//Create a new font and alter it.
	      XSSFFont font = workbook.createFont();
	      font.setBold(true);
	      //Set font into style
	      XSSFCellStyle style = workbook.createCellStyle();
	      style.setFont(font);
		return style;
	}
	
	public static void paymentWithDetailsHeader(Sheet sheet) {
	    Row newRow = sheet.createRow(0);
	    Cell cell = newRow.createCell(0);
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
	    cell.setCellValue("Payment");
	    XSSFCellStyle cellStyle = getStyleForHeader((XSSFWorkbook)sheet.getWorkbook());
	    cellStyle.setAlignment(HorizontalAlignment.CENTER);
	    cell.setCellStyle(cellStyle);
	        
	    cell = newRow.createCell(4);
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));
	    cell.setCellValue("Payment Details");
	    cell.setCellStyle(cellStyle);
	}

	public static void paymentDetailsHeader(Row row) {
		XSSFCellStyle style = getStyleForHeader((XSSFWorkbook)row.getSheet().getWorkbook());
		List<Object> paymentDetailsFields = Arrays.asList(new String[] { "Srl No","Event Name", "Month", "Year", "Amount" });

		for (int j = 0; j < paymentDetailsFields.size(); j++) {
			Cell cell = row.createCell(4 + j);
			cell.setCellValue((String) paymentDetailsFields.get(j));
			cell.setCellStyle(style);
		}
	}
	
	/// Expense related headers
	
	public static void expenseHeader(Sheet sheet, List<String> fields) {
		Row row = sheet.createRow(0);
		XSSFCellStyle style = getStyleForHeader((XSSFWorkbook)sheet.getWorkbook());

		for (int i = 0; i < fields.size(); i++) { 
			Cell cell = row.createCell(i);
			cell.setCellValue(fields.get(i));
			cell.setCellStyle(style);
		}
	}
	
	public static void expenseWithItemsHeader(Sheet sheet) {
	    Row newRow = sheet.createRow(0);
	    Cell cell = newRow.createCell(0);
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
	    cell.setCellValue("Voucher");
	    XSSFCellStyle cellStyle = getStyleForHeader((XSSFWorkbook)sheet.getWorkbook());
	    cellStyle.setAlignment(HorizontalAlignment.CENTER);
	    cell.setCellStyle(cellStyle);
	        
	    cell = newRow.createCell(4);
	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
	    cell.setCellValue("Voucher Items");
	    cell.setCellStyle(cellStyle);
	}
	
	public static void expenseItemsHeader(Row row) {
		XSSFCellStyle style = getStyleForHeader((XSSFWorkbook)row.getSheet().getWorkbook());
		List<Object> expenseItemsFields = Arrays.asList(new String[] { "Srl No","Item Head", "Amount" });

		for (int j = 0; j < expenseItemsFields.size(); j++) {
			Cell cell = row.createCell(4 + j);
			cell.setCellValue((String) expenseItemsFields.get(j));
			cell.setCellStyle(style);
		}
	}
}
