package com.ans.cda;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;

/**
 *
 * @author Leonardo Dias
 */
public class ConvertPdfToPdfA {

	/**
	 * convert pdf to pdfA
	 * @param pdfNormal
	 * @param pdfA
	 * @param autor
	 * @param assunto
	 */
	public static void convert(String pdfNormal, String pdfA, String autor, String assunto) {
		System.out.println("PDF to PDF/A");
		Document document = new Document();
		PdfReader reader;
		try {
			PdfAWriter writer = PdfAWriter.getInstance(document, new FileOutputStream(pdfA),
					PdfAConformanceLevel.PDF_A_1B);
			document.addAuthor(autor);
			document.addSubject(assunto);
			document.addLanguage("pt/br");
			document.addCreationDate();
			writer.setTagged();
			writer.createXmpMetadata();
			writer.setCompressionLevel(9);
			document.open();
			PdfContentByte pdfContentByte = writer.getDirectContent();
			reader = new PdfReader(pdfNormal);
			PdfImportedPage page;
			int pageCount = reader.getNumberOfPages();
			for (int i = 0; i < pageCount; i++) {
				final int index = i + 1;
				document.setPageSize(reader.getPageSize(index));
				document.newPage();
				page = writer.getImportedPage(reader, index);
				page.setBoundingBox(reader.getCropBox(index));
				pdfContentByte.addTemplate(page, 0, 0);
			}
		} catch (IOException | DocumentException ex) {
			ex.printStackTrace();
		}
		document.close();
	}

}
