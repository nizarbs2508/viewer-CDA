package com.ans.cda;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javafx.scene.control.TreeItem;

/**
 * Class Handler
 * 
 * @author bensa Nizar
 */
public class Handler extends DefaultHandler {

	TreeItem<String> item = new TreeItem<String>();

	private Stack<Element> elementStack = new Stack<Element>();

	private StringBuilder textBuffer = new StringBuilder();

	private Locator locator;

	private static Document doc;

	/**
	 * readXML
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public static TreeItem<String> readXML(final File file) throws IOException, SAXException {
		SAXParser parser;
		TreeItem<String> itemm;
		try {
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			parser = factory.newSAXParser();
			final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			doc = docBuilder.newDocument();
			Handler handler = new Handler();
			final XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(handler);
			parser.parse(file.toURI().toString(), handler);
			itemm = handler.item.getChildren().get(0);
			handler.item.getChildren().clear();

		} catch (final ParserConfigurationException e) {
			throw new RuntimeException("Can't create SAX parser / DOM builder.", e);
		}
		return itemm;

	}

	/**
	 * setDocumentLocator
	 */
	@Override
	public void setDocumentLocator(final Locator locator) {
		this.locator = locator;
	}

	/**
	 * startElement
	 */
	@Override
	public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
			throws SAXException {
		// call addTextIfNeeded function
		addTextIfNeeded();
		final Element el = doc.createElement(qName);
		final String node = doc.getNodeValue();
		for (int i = 0; i < attributes.getLength(); i++)
			el.setAttribute(attributes.getQName(i), attributes.getValue(i));
		el.setAttribute("lineNumAttribName", String.valueOf(locator.getLineNumber()));
		el.setTextContent(node);
		elementStack.push(el);
		// start a new node and use it as the current
		final TreeItem<String> item = new TreeItem<String>(qName + " [" + el.getAttribute("lineNumAttribName") + "]");
		this.item.getChildren().add(item);
		this.item = item;
	}

	/**
	 * endElement
	 */
	@Override
	public void endElement(final String uri, final String localName, final String qName) {
		// call addTextIfNeeded function
		addTextIfNeeded();
		final Element closedEl = elementStack.pop();
		if (elementStack.isEmpty()) { // Is this the root element?
			doc.appendChild(closedEl);
		} else {
			final Element parentEl = elementStack.peek();
			parentEl.appendChild(closedEl);
		}
		// finish this node by going back to the parent
		this.item = this.item.getParent();
	}

	/**
	 * characters
	 */
	@Override
	public void characters(final char ch[], final int start, final int length) throws SAXException {
		textBuffer.append(ch, start, length);
		final String s = String.valueOf(ch, start, length).trim();
		if (!s.isEmpty()) {
			// add text content as new child
			this.item.getChildren().add(new TreeItem<>(s));
		}
	}

	/**
	 * addTextIfNeeded Outputs text accumulated under the current node
	 */
	private void addTextIfNeeded() {
		if (textBuffer.length() > 0) {
			final Element el = elementStack.peek();
			final Node textNode = doc.createTextNode(textBuffer.toString());
			el.appendChild(textNode);
			textBuffer.delete(0, textBuffer.length());
		}
	}
}
