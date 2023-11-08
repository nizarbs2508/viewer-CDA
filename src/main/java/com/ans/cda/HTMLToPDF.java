package com.ans.cda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * HTMLToPDF
 * @author bensalem Nizar
 */
public class HTMLToPDF {

	/**
	 * transform xml to html with xslt
	 * 
	 * @param xslFile
	 * @param xmlFile
	 * @return strResult
	 */
	public static String transform(final String xslFile, final String xmlFile) {

		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		// attribut properties to make javafx working in heigher jre than 1.8
		transformerFactory.setAttribute(Constant.grp, "0");
		transformerFactory.setAttribute(Constant.expr_op_limit, "0");
		transformerFactory.setAttribute(Constant.tot_op_limit, "0");

		Transformer xform;

		final StringWriter writer = new StringWriter();
		String strResult = null;
		try {
			xform = transformerFactory.newTransformer(new StreamSource(new File(xslFile)));
			xform.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
			xform.setOutputProperty(OutputKeys.INDENT, "yes");
			xform.setOutputProperty(OutputKeys.METHOD, "html");
			xform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			xform.transform(new StreamSource(new File(xmlFile)), new StreamResult(writer));
			strResult = writer.toString();
		} catch (final TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (final TransformerException e) {
			e.printStackTrace();
		}
		return strResult;
	}

	/**
	 * void main
	 * @param file
	 * @param filexsl
	 * @return
	 */
	public static File main(final String file, final String filexsl) {
		File outputPdf = null;
		try {
			final Path pdf = Files.createTempFile(null, Constant.htmlext);
			final String streamRes = transform(filexsl, file);
			Files.write(pdf, streamRes.getBytes(StandardCharsets.UTF_8));
			// Source HTML file
			final String strFile = pdf.toString();
			final File inputHTML = new File(strFile);
			final File inputFile = inputHTML;
			final Path tempPath = Files.createTempFile(null, Constant.htmlext);
			final File tempFile = new File(tempPath.toString());

			final BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			final String lineToRemove = Constant.doctype;
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				// trim newline when comparing with lineToRemove
				String trimmedLine = currentLine.trim();
				if (trimmedLine.startsWith(lineToRemove))
					continue;
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.close();
			reader.close();

			final Path path = Paths.get(tempFile.getAbsolutePath());
			final Charset charset = StandardCharsets.UTF_8;

			String content = new String(Files.readAllBytes(path), charset);
			content = content.replaceAll("&nbsp;", " ");
			Files.write(path, content.getBytes(charset));
			// Generated PDF file name
			final Path pdfGenerated = Files.createTempFile(null, Constant.pdfext);
			final String pdfFile = pdfGenerated.toString();
			outputPdf = new File(pdfFile);
			// Convert HTML to XHTML
			final String xhtml = htmlToXhtml(new File(tempFile.getAbsolutePath()));
			System.out.println("Converting to PDF...");
			xhtmlToPdf(xhtml, outputPdf);

		} catch (final IOException e) {
			e.printStackTrace();
		}
		return outputPdf;
	}

	/**
	 * htmlToXhtml
	 * convert html to xhtml
	 * @param inputHTML
	 * @return
	 * @throws IOException
	 */
	private static String htmlToXhtml(final File inputHTML) throws IOException {
		final Document document = Jsoup.parse(inputHTML, "UTF-8");
		System.out.println("parsing ...");
		document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
		System.out.println("parsing done ...");
		return document.html();
	}

	/**
	 * xhtmlToPdf
	 * convert xhtml to pdf
	 * @param xhtml
	 * @param outputPdf
	 * @throws IOException
	 */
	private static void xhtmlToPdf(final String xhtml, final File outputPdf) throws IOException {
		final ITextRenderer renderer = new ITextRenderer();
		final SharedContext sharedContext = renderer.getSharedContext();
		sharedContext.setPrint(true);
		sharedContext.setInteractive(false);
		sharedContext.setReplacedElementFactory(new ImageReplacedElementFactory());
		sharedContext.getTextRenderer().setSmoothingThreshold(0);
		renderer.setDocumentFromString(xhtml, null);
		renderer.layout();
		final OutputStream outputStream = new FileOutputStream(outputPdf);
		renderer.createPDF(outputStream);
		System.out.println("PDF creation completed");
		outputStream.close();
	}
}
