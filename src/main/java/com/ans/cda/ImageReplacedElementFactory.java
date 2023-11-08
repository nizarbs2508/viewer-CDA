package com.ans.cda;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Image;

/**
 * ImageReplacedElementFactory
 * 
 * @author bensalem Nizar
 */
public class ImageReplacedElementFactory implements ReplacedElementFactory {

	private List<String> list = new ArrayList<String>();

	/**
	 * generate Image From PDF
	 * 
	 * @param filename
	 */
	private void generateImageFromPDF(final String filename) {
		PDDocument document;
		try {
			document = PDDocument.load(new File(filename));
			final PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int page = 0; page < document.getNumberOfPages(); ++page) {
				final BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
				final Path png = Files.createTempFile(null, Constant.pngext);
				final String strFile = png.toString();
				ImageIOUtil.writeImage(bim, String.format(strFile, page + 1), 300);
				list.add(strFile);
			}
			document.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * createReplacedElement
	 */
	@Override
	public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth,
			int cssHeight) {
		Element e = box.getElement();
		if (e == null) {
			return null;
		}
		String nodeName = e.getNodeName();
		if (nodeName.equals(Constant.object)) {
			String attribute = e.getAttribute(Constant.data);
			if (attribute.startsWith(Constant.startData)) {
				final String[] words = attribute.split(Constant.startB64);
				String base64String = words[1];
				if (base64String.contains(Constant.spc1)) {
					base64String = base64String.replaceAll(Constant.spc1, "");
				}
				if (base64String.contains(Constant.spc2)) {
					base64String = base64String.replaceAll(Constant.spc2, "");
				}
				final byte[] decodedBytes = Base64.getDecoder().decode(base64String.replaceAll(" ", ""));
				try {
					final Path pdf = Files.createTempFile(null, Constant.pdfext);
					final String strFile = pdf.toString();
					Files.write(Paths.get(strFile), decodedBytes);
					attribute = new File(strFile).getAbsolutePath();
					generateImageFromPDF(attribute);
					for (int i = 0; i < list.size(); i++) {
						attribute = new File(list.get(i)).getAbsolutePath();
						FSImage fsImage;
						try {
							fsImage = imageForPDF(attribute, uac);
						} catch (final BadElementException e1) {
							fsImage = null;
						} catch (final IOException e1) {
							fsImage = null;
						}
						if (fsImage != null) {
							if (cssWidth != -1 || cssHeight != -1) {
								fsImage.scale(cssWidth, cssHeight);
							} else {
								int width = 10000;
								int height = 13000;
								fsImage.scale(width, height);
							}
							return new ITextImageElement(fsImage);
						}
					}

				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		if (nodeName.equals(Constant.img)) {
			String attribute = e.getAttribute(Constant.src);
			if (attribute.startsWith(Constant.startData)) {
				final String[] words = attribute.split(Constant.startB64);
				String base64String = words[1];
				if (base64String.contains(Constant.spc1)) {
					base64String = base64String.replaceAll(Constant.spc1, "");
				}
				if (base64String.contains(Constant.spc2)) {
					base64String = base64String.replaceAll(Constant.spc2, "");
				}
				final byte[] decodedBytes = Base64.getDecoder().decode(base64String.replaceAll(" ", ""));
				try {
					final Path pdf = Files.createTempFile(null, Constant.pdfext);
					final String strFile = pdf.toString();
					Files.write(Paths.get(strFile), decodedBytes);
					attribute = new File(strFile).getAbsolutePath();
				} catch (final IOException e1) {
					e1.printStackTrace();
				}
			}
			FSImage fsImage;
			try {
				fsImage = imageForPDF(attribute, uac);
			} catch (final BadElementException e1) {
				fsImage = null;
			} catch (final IOException e1) {
				fsImage = null;
			}
			if (fsImage != null) {
				if (cssWidth != -1 || cssHeight != -1) {
					fsImage.scale(cssWidth, cssHeight);
				} else {
					int width = 6000;
					int height = 6000;
					fsImage.scale(width, height);
				}
				return new ITextImageElement(fsImage);
			}
		}
		return null;
	}

	protected FSImage imageForPDF(final String attribute, final UserAgentCallback uac)
			throws IOException, BadElementException {
		InputStream input = null;
		FSImage fsImage;
		input = new FileInputStream(attribute);
		final byte[] bytes = IOUtils.toByteArray(input);
		final Image image = Image.getInstance(bytes);
		fsImage = new ITextFSImage(image);
		return fsImage;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	@Override
	public void remove(Element e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setFormSubmissionListener(FormSubmissionListener listener) {
		// TODO Auto-generated method stub
	}
}