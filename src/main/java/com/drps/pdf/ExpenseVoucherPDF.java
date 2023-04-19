/**
 * 
 */
package com.drps.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.border.Border;

import org.springframework.util.StringUtils;

import com.drps.ams.entity.ExpenseItemsEntity;
import com.drps.ams.entity.ExpensesEntity;
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

import net.bytebuddy.asm.Advice.This;

/**
 * @author 002ZX2744
 *
 */
public class ExpenseVoucherPDF {

	private static String FILE = "C:/Users/002ZX2744/Desktop/TEMP/pdf/expanse-voucher.pdf";
	private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
	private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.RED);
	private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
	private static Font subFontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
	private static Font subFontBold = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
	private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

	public static void main(String[] args) {
		try {
			create();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static ExpensesEntity entity;
    private static List<ExpenseItemsEntity> expenseItemList;
    private static Map<String, Object> result;
    private static Map<Long, String> eventListMap;
    
    public ExpenseVoucherPDF(String filePath, ExpensesEntity entity, List<ExpenseItemsEntity> expenseItemList, 
    		Map<String, Object> result, Map<Long, String> eventListMap) {
    	this.FILE = filePath;
    	this.entity = entity;
    	this.expenseItemList = expenseItemList;
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
        document.setMargins(3f, 4f, 4f, 0f);
        
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();
        
        addRectTop(document);
        
        
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
     // Defiles the relative width of the columns
        float[] columnWidths = new float[]{50f, 50f};
        table.setWidths(columnWidths);
        
        addHeaderContent(table);
        addDateContent(table);
        
        addBodyContent(table);
        
        addFooterContent(table, 
        		"In Words Rupees:- " + NumberToWordsConverter.convertInWords(entity.getAmount().intValue()), 
        		"Mode of payment:-	" + (entity.getPaymentMode() != null ? entity.getPaymentMode() : ""));
        
        document.add(table);
        
        document.close();
   }
	
	private static void addRectTop(Document document) throws DocumentException {
		float llx = 592;
        float lly = 837; //355
        float urx = 3;
        float ury = 465;
        Rectangle rectBorder = new Rectangle(llx, lly, urx, ury);
        rectBorder.setBorderColor(BaseColor.BLACK);
        rectBorder.setBorder(Rectangle.BOX);
        rectBorder.setBorderWidth(1);
        document.add(rectBorder);
	}
	
	private static void addHeaderContent(PdfPTable table) {
		boolean isCanceled = entity.getIsCanceled() != null ? entity.getIsCanceled() : false;
		
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(isCanceled ? 75f : 60f);
    	//cellOne.setFixedHeight(60f);
    	cellOne.setPaddingTop(5f);
    	if(isCanceled) {
    		cellOne.addElement(addCenterTextForHeader("*********** Voucher Canceled ***********", subFontNormal));	
    	}
    	cellOne.addElement(addCenterTextForHeader("FLAT OWNER'S ASSOCIATION", titleFont));
    	cellOne.addElement(addCenterTextForHeader("Economy Apartment, Jhill Park, 48, J.C. Khan Road, Mankundu, Hooghly", subFontNormal));
    	cellOne.addElement(addCenterTextForHeader("DEBIT VOUCHER", subFontBold));
    	cellOne.setBorder(Rectangle.BOTTOM);
    	cellOne.setColspan(2);
        table.addCell(cellOne);
    }
	
	private static void addDateContent(PdfPTable table) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setPaddingBottom(5f);
    	cellOne.addElement(addLeftAlignText("Date: " + DateUtils.dateTimeToString(null), normalFont));
    	cellOne.setBorder(Rectangle.BOTTOM);
//    	cellOne.setColspan(2);
        table.addCell(cellOne);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setPaddingBottom(5f);
        cellTwo.addElement(addLeftAlignText("Voucher No: " + (entity.getVoucherNo() != null ? entity.getVoucherNo() : "") , normalFont));
    	
        cellTwo.setBorder(Rectangle.BOTTOM);
//        cellTwo.setColspan(2);
        table.addCell(cellTwo);
    }
	
	private static void addBodyContent(PdfPTable table) throws DocumentException {


        PdfPTable tblBody = new PdfPTable(3);
        tblBody.setWidthPercentage(100);
     // Defiles the relative width of the columns
        float[] columnWidths = new float[]{10f, 75f, 15f};
        tblBody.setWidths(columnWidths);
		
        addBodyContentTitle(tblBody);
        addBodyContentDesc(tblBody);
        
        boolean isCanceled = entity.getIsCanceled() != null ? entity.getIsCanceled() : false;
        int totalLineCount = isCanceled ? 9 : 10;
        int count = 1;
        if(!Objects.isNull(expenseItemList)) {
        	for(ExpenseItemsEntity itemEntity : expenseItemList) {
        		if(itemEntity != null) {
        			addItem(tblBody, count++, itemEntity.getItemHead(), itemEntity.getAmount());
                	totalLineCount--;
        		}
        		
        	}
        }
        
        for(int i=0; i<totalLineCount; i++) {
        	addBlankItem(tblBody);
        }
       
        double tptalAmount = Objects.isNull(expenseItemList) ? 0.0 : expenseItemList.stream().mapToDouble(ExpenseItemsEntity::getAmount).sum();
        addTotalAmountItem(tblBody, tptalAmount);
        
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setPadding(0f);
    	cellOne.addElement(tblBody);
    	cellOne.setBorder(Rectangle.BOTTOM);
    	cellOne.setColspan(2);
        table.addCell(cellOne);
    }
	
	private static void addFooterContent(PdfPTable table, String amountInWord, String modeOfPayment) throws DocumentException {


        PdfPTable tblBody = new PdfPTable(3);
        tblBody.setWidthPercentage(100);
     // Defiles the relative width of the columns
        float[] columnWidths = new float[]{10f, 75f, 15f};
        tblBody.setWidths(columnWidths);
		
        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(0f);
        cell1.addElement(addLeftAlignText(amountInWord, normalFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setColspan(3);
        tblBody.addCell(cell1);
        
        PdfPCell cell2 = new PdfPCell();
        cell2.setPadding(0f);
        cell2.addElement(addLeftAlignText(modeOfPayment, normalFont));
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setColspan(3);
        tblBody.addCell(cell2);
        
        PdfPCell cell3 = new PdfPCell();
        cell3.setPadding(14f);
        cell3.addElement(addLeftAlignText(
        		"              Treasurer                                             						   Secretary                                                        					                 Received By", normalFont));
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setColspan(3);
        tblBody.addCell(cell3);
        
        
    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setPadding(0f);
    	cellOne.addElement(tblBody);
    	cellOne.setBorder(Rectangle.NO_BORDER);
    	cellOne.setColspan(2);
        table.addCell(cellOne);
    }
	
	private static void addBodyContentTitle(PdfPTable table) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setPaddingBottom(5f);
    	cellOne.addElement(addCenterAlignText("Description on accounts of following heads ", normalFont));
    	cellOne.setBorder(Rectangle.BOTTOM);
    	cellOne.setColspan(2);
        table.addCell(cellOne);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setPaddingBottom(5f);
        cellTwo.addElement(addCenterAlignText("Amount ", normalFont));
//        cellTwo.setBorder(Rectangle.LEFT);
        table.addCell(cellTwo);
    }
	
	private static void addBodyContentDesc(PdfPTable table) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(55f);
    	cellOne.setPaddingBottom(5f);
    	cellOne.addElement(addLeftAlignText("Debit: " + (entity.getTitle() == null ? "" : entity.getTitle()), normalFont));
    	cellOne.addElement(addLeftAlignText("A/C....." + (entity.getAccountNo() == null ? "" : entity.getAccountNo()), normalFont));
    	cellOne.addElement(addLeftAlignText("Beling the amount paid to............................................................. ", normalFont));
    	cellOne.setBorder(Rectangle.NO_BORDER);
    	cellOne.setColspan(2);
        table.addCell(cellOne);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setPaddingBottom(5f);
//        cellTwo.addElement(addCenterAlignText("Amount ", normalFont));
        cellTwo.setBorder(Rectangle.LEFT);
        table.addCell(cellTwo);
    }
	
	private static void addItem(PdfPTable table, int srlNo, String text, double amount) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setPaddingTop(-2f);
    	cellOne.setPaddingBottom(2f);
    	cellOne.addElement(addLeftAlignText(srlNo, normalFont));
    	cellOne.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellOne);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setPaddingTop(-2f);
        cellTwo.setPaddingBottom(2f);
        cellTwo.addElement(addLeftAlignText(text, normalFont));
        cellTwo.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellTwo);
        
        PdfPCell cellThree = new PdfPCell();
        cellThree.setPaddingTop(-2f);
        cellThree.setPaddingBottom(2f);
        cellThree.addElement(addRightAlignText(amount, normalFont));
        cellThree.setBorder(Rectangle.LEFT);
        table.addCell(cellThree);
    }

	private static void addBlankItem(PdfPTable table) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setPaddingTop(-2f);
    	cellOne.setPaddingBottom(2f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.addElement(addLeftAlignText(new Paragraph("  "), normalFont));
    	cellOne.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellOne);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setPaddingTop(-2f);
        cellTwo.setPaddingBottom(2f);
        cellTwo.addElement(addLeftAlignText(new Paragraph("  "), normalFont));
        cellTwo.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellTwo);
        
        PdfPCell cellThree = new PdfPCell();
        cellThree.setPaddingTop(-2f);
        cellThree.setPaddingBottom(2f);
        cellThree.addElement(addRightAlignText(new Paragraph("  "), normalFont));
        cellThree.setBorder(Rectangle.LEFT);
        table.addCell(cellThree);
    }
	
	private static void addTotalAmountItem(PdfPTable table, double total) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setPaddingTop(-2f);
    	cellOne.setPaddingBottom(2f);
    	cellOne.setPaddingLeft(2f);
    	cellOne.addElement(addLeftAlignText(new Paragraph("  "), normalFont));
    	cellOne.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellOne);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setPaddingTop(-2f);
        cellTwo.setPaddingBottom(2f);
        cellTwo.addElement(addRightAlignText("Total", normalFont));
        cellTwo.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellTwo);
        
        PdfPCell cellThree = new PdfPCell();
        cellThree.setPaddingTop(-2f);
        cellThree.setPaddingBottom(2f);
        cellThree.addElement(addRightAlignText(total, normalFont));
//        cellThree.setBorder(Rectangle.LEFT);
        table.addCell(cellThree);
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
        	paragraph.setLeading(14f);
        	
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
        	paragraph.setLeading(10f);
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
}
