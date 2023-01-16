package com.drps.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.drps.ams.entity.EventsEntity;
import com.drps.ams.entity.PaymentDetailsEntity;
import com.drps.ams.entity.PaymentEntity;
import com.drps.ams.util.DateUtils;
import com.drps.ams.util.NumberToWordsConverter;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class PaymentReceiptPDF {
    private static String FILE = "C:/Users/002ZX2744/Desktop/TEMP/pdf/payment-receipt.pdf";
    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static Font subFontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

    private static PaymentEntity entity;
    private static List<PaymentDetailsEntity> paymentItemList;
    private static Map<String, Object> result;
    private static Map<Long, String> eventListMap;
    
    public PaymentReceiptPDF(String filePath, PaymentEntity entity, List<PaymentDetailsEntity> paymentItemList, 
    		Map<String, Object> result, Map<Long, String> eventListMap) {
    	this.FILE = filePath;
    	this.entity = entity;
    	this.paymentItemList = paymentItemList;
    	this.result = result;
    	this.eventListMap = eventListMap;
    }
    
    public File getFile() {
    	try {
    		create();
    		return new File(FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}    	
		return null;
    }
    
    private static void create() throws FileNotFoundException, DocumentException {
    	 Document document = new Document(PageSize.A4);
         document.setMargins(3f, 5f, 3f, 0f);
         
         PdfWriter.getInstance(document, new FileOutputStream(FILE));
         document.open();
         
         addRectLeft(document);
         addRectRight(document);
         
         
         addMetaData(document);
         
         PdfPTable table = new PdfPTable(3);
         table.setWidthPercentage(100);
      // Defiles the relative width of the columns
         float[] columnWidths = new float[]{46.8f, 3.2f, 46.8f};
         table.setWidths(columnWidths);
         
         addHeader(table);
         addContent(table);
         document.add(table);
         
         document.close();
    }
    
    
    public static void main(String[] args) {
        try {
        	create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void addRectLeft(Document document) throws DocumentException {
		float llx = 287;
        float lly = 839; //355
        float urx = 3;
        float ury = 465;
        Rectangle rectBorder = new Rectangle(llx, lly, urx, ury);
        rectBorder.setBorderColor(BaseColor.BLACK);
        rectBorder.setBorder(Rectangle.BOX);
        rectBorder.setBorderWidth(1);
        document.add(rectBorder);
	}
	
	private static void addRectRight(Document document) throws DocumentException {
//		float llx = 287;
//        float lly = 839; //355
//        float urx = 3;
//        float ury = 465;
		
		
		float llx = 590;
        float lly = 839; //355
        float urx = 306;
        float ury = 465;
        Rectangle rectBorder = new Rectangle(llx, lly, urx, ury);
        rectBorder.setBorderColor(BaseColor.BLACK);
        rectBorder.setBorder(Rectangle.BOX);
        rectBorder.setBorderWidth(1);
        document.add(rectBorder);
	}
	
	private static void addHeader(PdfPTable table) {

		addHeaderContent(table);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellTwo);
        
        addHeaderContent(table);
	}
    
    private static <T> Paragraph addCenterTextForHeader(T obj, Font font) {
    	Paragraph paragraph = null;
    	if(Paragraph.class == obj.getClass()) {
    		paragraph = (Paragraph)obj;
    	} else if(String.class == obj.getClass()) {
    		paragraph = new Paragraph((String)obj);
    	} else {
    		paragraph = new Paragraph(obj.toString());
    	}
    	
    	if(paragraph != null) {
    		if(font != null) {
    			paragraph.setFont(font);
    		}
        	paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        	paragraph.setLeading(16f);
    	}		
    	return paragraph;
    }
    
    private static <T> Paragraph addCenterAlignText(T obj, Font font) {
    	Paragraph paragraph = null;
    	if(Paragraph.class == obj.getClass()) {
    		paragraph = (Paragraph)obj;
    	} else if(String.class == obj.getClass()) {
    		paragraph = new Paragraph((String)obj);
    	} else {
    		paragraph = new Paragraph(obj.toString());
    	}
    	
    	if(paragraph != null) {
    		if(font != null) {
    			paragraph.setFont(font);
    		}
        	paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        	paragraph.setLeading(12f);
    	}		
    	return paragraph;
    }
    
    private static <T> Paragraph addLeftAlignText(T obj, Font font) {
    	Paragraph paragraph = null;
    	if(Paragraph.class == obj.getClass()) {
    		paragraph = (Paragraph)obj;
    	} else if(String.class == obj.getClass()) {
    		paragraph = new Paragraph((String)obj);
    	} else {
    		paragraph = new Paragraph(obj.toString());
    	}
    	
    	if(paragraph != null) {
    		if(font != null) {
    			paragraph.setFont(font);
    		}
        	paragraph.setAlignment(Paragraph.ALIGN_LEFT);
        	paragraph.setLeading(12f);
        	
    	}		
    	return paragraph;
    }
    
    private static <T> Paragraph addRightAlignText(T obj, Font font) {
    	Paragraph paragraph = null;
    	if(Paragraph.class == obj.getClass()) {
    		paragraph = (Paragraph)obj;
    	} else if(String.class == obj.getClass()) {
    		paragraph = new Paragraph((String)obj);
    	} else {
    		paragraph = new Paragraph(obj.toString());
    	}
    	
    	if(paragraph != null) {
    		if(font != null) {
    			paragraph.setFont(font);
    		}
        	paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
        	paragraph.setLeading(12f);
        	
    	}		
    	return paragraph;
    }
    
    private static void addHeaderContent(PdfPTable table) {
    	boolean isCanceled = entity.getIsCanceled() != null ? entity.getIsCanceled() : false;
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(isCanceled ? 75f : 60f);
    	cellOne.setPaddingTop(5f);
    	if(isCanceled) {
    		cellOne.addElement(addCenterTextForHeader("*********** Payment Canceled ***********", subFontNormal));	
    	}
    	cellOne.addElement(addCenterTextForHeader("FLAT OWNER'S ASSOCIATION", titleFont));
    	cellOne.addElement(addCenterTextForHeader("Economy Apartment, Jhill Park", subFontNormal));
    	cellOne.addElement(addCenterTextForHeader("48, J.C. Khan Road, Mankundu, Hooghly", subFontNormal));
        table.addCell(cellOne);
    }
    
    private static void addContentHeader(PdfPTable table) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(60f);
    	cellOne.setPaddingTop(5f);
    	cellOne.addElement(addCenterTextForHeader("FLAT OWNER'S ASSOCIATION", titleFont));
    	cellOne.addElement(addCenterTextForHeader("Economy Apartment, Jhill Park", subFontNormal));
    	cellOne.addElement(addCenterTextForHeader("48, J.C. Khan Road, Mankundu, Hooghly", subFontNormal));
    	//Paragraph paragraph = addCenterText("FLAT OWNER'S ASSOCIATION", titleFont);
        //addEmptyLine(paragraph, 5);
        //cellOne.addElement(paragraph);
    	
        table.addCell(cellOne);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellTwo);
    }
    
    private static void addBillNoAndSession(PdfPTable table) {
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setBorder(PdfPCell.BOTTOM);
    	cellOne.setFixedHeight(14f);
    	cellOne.setPaddingTop(-2f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.addElement(addLeftAlignText("Bill No: " + entity.getBillNo(), normalFont));
    	table.addCell(cellOne);
    	
    	String sessionName = (String)result.getOrDefault("sessionName", "");
    	PdfPCell cellTwo = new PdfPCell();
    	cellTwo.setBorder(PdfPCell.LEFT);
    	cellTwo.setFixedHeight(14f);
    	cellTwo.setPaddingTop(-2f);
    	cellTwo.setPaddingLeft(2f);
    	cellTwo.addElement(addLeftAlignText("Session: "+sessionName, normalFont));
    	table.addCell(cellTwo);
    }
    
    private static void addContentRow1(PdfPTable table) {
    	String flatNo = (String)result.getOrDefault("flatNo", "");
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setBorder(PdfPCell.RIGHT);
    	cellOne.setFixedHeight(14f);
    	cellOne.setPaddingTop(-2f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.addElement(addLeftAlignText("Flat No: "+flatNo, normalFont));
    	table.addCell(cellOne);
    	
    	String mobileNo = (String)result.getOrDefault("mobileNo", "");
    	PdfPCell cellTwo = new PdfPCell();
    	cellTwo.setBorder(PdfPCell.TOP);
    	cellTwo.setFixedHeight(14f);
    	cellTwo.setPaddingTop(-2f);
    	cellTwo.setPaddingLeft(2f);
    	cellTwo.addElement(addLeftAlignText("Mobile No: "+mobileNo, normalFont));
    	table.addCell(cellTwo);
    }
    
    private static void addContentRow2(PdfPTable table) {
    	String ownersName = (String)result.getOrDefault("ownersName", "");
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(14f);
    	cellOne.setPaddingTop(0f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.setColspan(2);
    	cellOne.addElement(addLeftAlignText("Owner's Name: "+ownersName, normalFont));
    	table.addCell(cellOne);
    }
    
    private static void addContentRow3(PdfPTable table) {
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(14f);
    	cellOne.setPaddingTop(0f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.addElement(addLeftAlignText("Payment Date: ", normalFont));
    	table.addCell(cellOne);
    	
    	PdfPCell cellTwo = new PdfPCell();
    	cellTwo.setFixedHeight(14f);
    	cellTwo.setPaddingTop(0f);
    	cellTwo.setPaddingLeft(2f);
    	cellTwo.addElement(addLeftAlignText(DateUtils.dateToString(entity.getPaymentDate()), normalFont));
    	table.addCell(cellTwo);
    }
    
    public static String getMaintenanceDetails() {
    	Comparator<PaymentDetailsEntity> compareByYearThenMonth = Comparator
                .comparing(PaymentDetailsEntity::getPaymentYear)
                .thenComparing(PaymentDetailsEntity::getPaymentMonth);
    	
    	List<PaymentDetailsEntity> maintenanceList = paymentItemList.stream().filter( f -> f.getEventId() == 1).collect(Collectors.toList());
    	if(maintenanceList.isEmpty()) {
    		return null;
    	}
    	PaymentDetailsEntity firstPayment =  maintenanceList.stream().sorted(compareByYearThenMonth).limit(1).findAny().orElse(null);
    	if(maintenanceList.size() > 1) {
    		PaymentDetailsEntity lastPayment =  maintenanceList.stream().sorted(compareByYearThenMonth.reversed()).limit(1).findAny().orElse(null);
        	System.out.println(DateUtils.getMonthName(firstPayment.getPaymentMonth()-1));
        	System.out.println(DateUtils.getMonthName(lastPayment.getPaymentMonth()-1));
        	
        	String itemHead = "Maintenance for " + DateUtils.getMonthName(firstPayment.getPaymentMonth()-1) + ", " + firstPayment.getPaymentYear()
        					+" to " + DateUtils.getMonthName(lastPayment.getPaymentMonth()-1) + ", " + lastPayment.getPaymentYear();
        	double mntnceTot = maintenanceList.stream().map(PaymentDetailsEntity::getAmount).collect(Collectors.summingDouble(Double::doubleValue));
        	return itemHead + "@@##" + mntnceTot;
    	} else {
    		String itemHead = "Maintenance for " + DateUtils.getMonthName(firstPayment.getPaymentMonth()-1) + ", " + firstPayment.getPaymentYear();
    		return itemHead + "@@##" + firstPayment.getAmount();
    	}
    	
    	
    }
    
    private static void addContentRow4(PdfPTable table) throws DocumentException {
    	PdfPCell cellOne = new PdfPCell();
    	//cellOne.setFixedHeight(100f);
    	cellOne.setBorder(PdfPCell.NO_BORDER);
    	cellOne.setPaddingTop(0f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.setColspan(2);
    	
    	
    	PdfPTable itemTable = new PdfPTable(3);
    	itemTable.setWidthPercentage(101.5f);
     // Defiles the relative width of the columns
        float[] columnWidths = new float[]{15f, 65f, 20f};
        itemTable.setWidths(columnWidths);   
        
        addItemHeader(itemTable);
        
        boolean isCanceled = entity.getIsCanceled() != null ? entity.getIsCanceled() : false;
        int maxRowCount = isCanceled ? 9 : 10;
        addItemEmptyRow(itemTable, 1);
//        addItemRow(itemTable, 1, "Maintenance for Jul, 2022 to Oct, 2022(4 Months)", 2000);
//        addItemRow(itemTable, 2, "Development", 2200);
//        addItemRow(itemTable, 3, "Car Parking", 200);
//        addItemRow(itemTable, 4, "Puja Donation", 1200);
        
		int srlId = 1;
        String mntncDtls = getMaintenanceDetails();
        if(mntncDtls != null) {
        	maxRowCount = maxRowCount - 1;
        	String strArr[] = mntncDtls.split("@@##");
        	addItemRow(itemTable, srlId++, strArr[0], Double.parseDouble(strArr[1]));
        }
        List<PaymentDetailsEntity> mislnsList = paymentItemList.stream().filter( f -> f.getEventId() != 1).collect(Collectors.toList());
        maxRowCount = maxRowCount - mislnsList.size();
        
        for(PaymentDetailsEntity item : mislnsList) {
        	addItemRow(itemTable, srlId++, eventListMap.getOrDefault(item.getEventId(), ""), item.getAmount());
        }
        
        addItemEmptyRow(itemTable, maxRowCount);
        addItemTotalRow(itemTable, entity.getAmount());
        addItemRow(itemTable, "In words Rupees: " + NumberToWordsConverter.convertInWords(entity.getAmount().intValue())+".");
        
        String modeOfPay = entity.getPaymentMode();
        modeOfPay += "CHEQUE".equalsIgnoreCase(modeOfPay) && entity.getPaymentModeRef() != null ? "-" + entity.getPaymentModeRef() : "";
        addItemRow(itemTable, "Mode of payments: " + modeOfPay);
        //addItemRow(itemTable, "Remarks: ");
        addEmptyRow(itemTable, 1);
        addItemRowForSignature(itemTable, "Authority Signature");
    	cellOne.addElement(itemTable);  
    	
    	
    	table.addCell(cellOne);
    }
    
    private static void addItemHeader(PdfPTable table) {
    	PdfPCell cellOne = new PdfPCell();
//    	cellOne.setBorder(PdfPCell.RIGHT);
    	cellOne.setFixedHeight(14f);
    	cellOne.setPaddingTop(0f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.addElement(addLeftAlignText("Srl. No.", normalFont));
    	table.addCell(cellOne);
    	
    	PdfPCell cellTwo = new PdfPCell();
//    	cellTwo.setBorder(PdfPCell.RIGHT);
    	cellTwo.setFixedHeight(14f);
    	cellTwo.setPaddingTop(0f);
    	cellTwo.setPaddingLeft(2f);
    	cellTwo.addElement(addCenterAlignText("Description", normalFont));
    	table.addCell(cellTwo);
    	
    	PdfPCell cellThree = new PdfPCell();
//    	cellThree.setBorder(PdfPCell.NO_BORDER);
    	cellThree.setFixedHeight(14f);
    	cellThree.setPaddingTop(0f);
    	cellThree.setPaddingLeft(2f);
    	cellThree.addElement(addRightAlignText("Amount", normalFont));
    	table.addCell(cellThree);
    }
    
    private static void addItemRow(PdfPTable table, int rowNo, String itemHead, double amount) {
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setBorder(PdfPCell.NO_BORDER);
//    	cellOne.setFixedHeight(14f);
    	cellOne.setPaddingTop(0f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.addElement(addRightAlignText(rowNo+".", normalFont));
    	table.addCell(cellOne);
    	
    	PdfPCell cellTwo = new PdfPCell();
    	cellTwo.setBorder(PdfPCell.NO_BORDER);
    	//cellTwo.setFixedHeight(14f);
    	cellTwo.setPaddingTop(0f);
    	cellTwo.setPaddingLeft(2f);
    	cellTwo.addElement(addLeftAlignText(itemHead, normalFont));
    	table.addCell(cellTwo);
    	
    	PdfPCell cellThree = new PdfPCell();
    	cellThree.setBorder(PdfPCell.LEFT);
//    	cellThree.setFixedHeight(14f);
    	cellThree.setPaddingTop(0f);
    	cellThree.setPaddingLeft(2f);
    	cellThree.addElement(addRightAlignText(amount, normalFont));
    	table.addCell(cellThree);
    }
    
    private static void addItemEmptyRow(PdfPTable table, int rowCount) {
    	
    	for(int i=0; i<rowCount; i++) {
    		PdfPCell cellOne = new PdfPCell();
        	cellOne.setBorder(PdfPCell.NO_BORDER);
        	cellOne.setFixedHeight(14f);
        	cellOne.setPaddingTop(0f);
        	cellOne.setPaddingLeft(2f);
        	cellOne.setColspan(2);
        	cellOne.addElement(addRightAlignText("", normalFont));
        	table.addCell(cellOne);
        	
        	PdfPCell cellThree = new PdfPCell();
        	cellThree.setBorder(PdfPCell.LEFT);
        	cellThree.setFixedHeight(14f);
        	cellThree.setPaddingTop(0f);
        	cellThree.setPaddingLeft(2f);
        	cellThree.addElement(addRightAlignText("", normalFont));
        	table.addCell(cellThree);
    	}
    	
    	
    }
    
    private static void addEmptyRow(PdfPTable table, int rowCount) {
    	
    	for(int i=0; i<rowCount; i++) {
    		PdfPCell cellOne = new PdfPCell();
        	cellOne.setBorder(PdfPCell.NO_BORDER);
        	cellOne.setFixedHeight(14f);
        	cellOne.setPaddingTop(0f);
        	cellOne.setPaddingLeft(2f);
        	cellOne.setColspan(3);
        	cellOne.addElement(addRightAlignText("", normalFont));
        	table.addCell(cellOne);
    	}
    	
    	
    }
    
    private static void addItemTotalRow(PdfPTable table, double totalAmount) {
    	PdfPCell cellOne = new PdfPCell();
//    	cellOne.setBorder(PdfPCell.NO_BORDER);
    	cellOne.setFixedHeight(14f);
    	cellOne.setPaddingTop(0f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.setColspan(2);
    	cellOne.addElement(addRightAlignText("Total: ", normalFont));
    	table.addCell(cellOne);
    	
    	PdfPCell cellThree = new PdfPCell();
//    	cellThree.setBorder(PdfPCell.LEFT);
    	cellThree.setFixedHeight(14f);
    	cellThree.setPaddingTop(0f);
    	cellThree.setPaddingLeft(2f);
    	cellThree.addElement(addRightAlignText(totalAmount, normalFont));
    	table.addCell(cellThree);
    }
    
	private static void addItemRow(PdfPTable table, String text) {
    	
		PdfPCell cellOne = new PdfPCell();
		cellOne.setBorder(PdfPCell.NO_BORDER);
		//cellOne.setFixedHeight(14f);
		cellOne.setMinimumHeight(16f);
		cellOne.setPaddingTop(0f);
		cellOne.setPaddingLeft(2f);
		cellOne.setColspan(3);
		cellOne.addElement(addLeftAlignText(text, normalFont));
		table.addCell(cellOne);
    }
	
	private static void addItemRowForSignature(PdfPTable table, String text) {
    	
		PdfPCell cellOne = new PdfPCell();
		cellOne.setBorder(PdfPCell.NO_BORDER);
		cellOne.setFixedHeight(14f);
		cellOne.setPaddingTop(0f);
		cellOne.setPaddingLeft(2f);
		cellOne.setColspan(3);
		cellOne.addElement(addRightAlignText(text, normalFont));
		table.addCell(cellOne);
    }
	
	private static void addContent(PdfPTable table) throws DocumentException {
        
        addContentData(table);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellTwo);
        
        addContentData(table);
    }
    
    private static void addContentData(PdfPTable table) throws DocumentException {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(333f);
//    	cellOne.setPaddingTop(5f);
    	cellOne.setBorder(Rectangle.NO_BORDER);
    	
    	
    	
    	PdfPTable contentTable = new PdfPTable(2);
    	contentTable.setWidthPercentage(101.5f);
     // Defiles the relative width of the columns
        float[] columnWidths = new float[]{50f, 50f};
        contentTable.setWidths(columnWidths);   
        
        addBillNoAndSession(contentTable);
        addContentRow1(contentTable);
        addContentRow2(contentTable);
        addContentRow3(contentTable);
        addContentRow4(contentTable);
    	cellOne.addElement(contentTable);    	
    	
        table.addCell(cellOne);
    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
    private static void addMetaData(Document document) {
        document.addTitle("Payment Receipt");
        document.addSubject("Payment Details Receipt");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Pradyut Sarkar");
        document.addCreator("Pradyut Sarkar");
    }


    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}