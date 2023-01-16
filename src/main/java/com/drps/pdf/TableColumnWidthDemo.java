package com.drps.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TableColumnWidthDemo {
	private static String FILE = "C:/Users/002ZX2744/Desktop/TEMP/pdf/TableColumnWidth.pdf";
	
    public static void main(String[] args) {
        Document doc = new Document();
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(FILE));
            doc.open();

            PdfPTable table = new PdfPTable(4);
            table.addCell(new PdfPCell(new Phrase("Cell 1")));
            table.addCell(new PdfPCell(new Phrase("Cell 2")));
            table.addCell(new PdfPCell(new Phrase("Cell 3")));
            table.addCell(new PdfPCell(new Phrase("Cell 4")));

            // Defiles the relative width of the columns
            float[] columnWidths = new float[]{10f, 20f, 30f, 10f};
            table.setWidths(columnWidths);

            doc.add(table);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }
}
