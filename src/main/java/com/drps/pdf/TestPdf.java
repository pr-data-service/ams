package com.drps.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TestPdf {
	
	private static String FILE = "C:/Users/002ZX2744/Desktop/TEMP/pdf/test.pdf";
    private static Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static Font subFontNormal = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);

	public static void main(String[] args) throws FileNotFoundException, DocumentException {
		create();
	}

	private static void create() throws FileNotFoundException, DocumentException {
   	 Document document = new Document(PageSize.A4);
        document.setMargins(3f, 5f, 3f, 0f);
        
        PdfWriter.getInstance(document, new FileOutputStream(FILE));
        document.open();
        
        addRectLeft(document);
        addRectRight(document);
        
        
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
     // Defiles the relative width of the columns
        float[] columnWidths = new float[]{46.8f, 3.2f, 46.8f};
        table.setWidths(columnWidths);
        
        addHeader(table);
       
        document.add(table);
        
        document.close();
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

		addContent(table);
        
        PdfPCell cellTwo = new PdfPCell();
        cellTwo.setBorder(Rectangle.NO_BORDER);
        table.addCell(cellTwo);
        
        addContent(table);
	}
	
	private static void addContent(PdfPTable table) {

    	PdfPCell cellOne = new PdfPCell();
    	cellOne.setFixedHeight(60f);
    	cellOne.setPaddingTop(5f);
    	cellOne.addElement(addCenterTextForHeader("FLAT OWNER'S ASSOCIATION", titleFont));
    	cellOne.addElement(addCenterTextForHeader("Economy Apartment, Jhill Park", subFontNormal));
    	cellOne.addElement(addCenterTextForHeader("48, J.C. Khan Road, Mankundu, Hooghly", subFontNormal));
        table.addCell(cellOne);
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
}
