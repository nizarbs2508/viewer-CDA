package com.ans.cda;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aspose.barcode.generation.BarCodeImageFormat;
import com.aspose.barcode.generation.BarcodeGenerator;
import com.aspose.barcode.generation.DataMatrixEncodeMode;
import com.aspose.barcode.generation.EncodeTypes;
import com.sun.webkit.WebPage;

import gui.components.MainMenuBar;
import gui.mediator.IMediator;
import gui.mediator.Mediator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Separator;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import lib.EditorUtils;

/**
 * WebViewSample api with JavaFX
 * 
 * @author bensalem Nizar
 */
public class WebViewSample extends Application {
	/**
	 * browser
	 */
	private WebView browser = new WebView();
	/**
	 * webEngine
	 */
	public WebEngine webEngine = browser.getEngine();
	/**
	 * outputFileName
	 */
	private String outputFileName = "";
	/**
	 * desktop
	 */
	private Desktop desktop = Desktop.getDesktop();
	/**
	 * xmlButton
	 */
	private Button xmlButton = I18N.buttonForKey("button.selectcda");

	/**
	 * xslButton
	 */
	private Button xslButton = I18N.buttonForKey("button.selectxsl");
	/**
	 * validateButton
	 */
	private Button validateButton = I18N.buttonForKey("button.displaycda");
	/**
	 * openButton
	 */
	private Button openButton = I18N.buttonForKey("button.opennav");
	/**
	 * pdfButton
	 */
	private Button pdfButton = I18N.buttonForKey("button.generatepdf");
	/**
	 * showXmlButton
	 */
	private Button showXmlButton = I18N.buttonForKey("button.showcda");
	/**
	 * clearButton
	 */
	private Button clearButton = I18N.buttonForKey("button.initinterface");
	/**
	 * openWithButton
	 */
	private Button openWithButton = I18N.buttonForKey("button.openwith");
	/**
	 * button
	 */
	private Button button = I18N.buttonForKey("button.search.text");
	/**
	 * buttonX
	 */
	private Button buttonX = new Button("x");
	/**
	 * textField
	 */
	private TextField textField = new TextField();
	/**
	 * textFieldxsl
	 */
	public TextField textFieldxsl = new TextField();
	/**
	 * primaryStage
	 */
	private Stage primaryStage;
	/**
	 * number of language switches
	 */
	private Integer numSwitches = 0;
	/**
	 * menuBar for browser
	 */
	private MenuBar menuBar = new MenuBar();
	/**
	 * mediator
	 */
	private IMediator mediator = Mediator.getInstance();
	/**
	 * map
	 */
	public HashMap<String, String> map = new HashMap<String, String>();
	/**
	 * mapXsl
	 */
	public HashMap<String, String> mapXsl = new HashMap<String, String>();
	/**
	 * history
	 */
	public WebHistory history = browser.getEngine().getHistory();
	/**
	 * exist
	 */
	public boolean exist = false;
	/**
	 * isAutopresentable
	 */
	public boolean isAutopresentable = false;
	/**
	 * DEFAULT_JQUERY_MIN_VERSION
	 */
	public static final String DEFAULT_JQUERY_MIN_VERSION = "1.7.2";
	/**
	 * JQUERY_LOCATION
	 */
	public static final String JQUERY_LOCATION = "http://code.jquery.com/jquery-1.7.2.min.js";

	/**
	 * void main for Javafx launcher Main secondaire de l'application de javaFX
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		launch(args);
	}

	/**
	 * transform xml to html with xslt
	 * 
	 * @param xslFile
	 * @param xmlFile
	 * @return strResult
	 */
	public String transform(final String xslFile, final String xmlFile) {

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
	 * loading in api
	 */
	public void runTask(final Stage taskUpdateStage, final ProgressIndicator progress) {
		Task<Void> longTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				final int max = 20;
				for (int i = 1; i <= max; i++) {
					if (isCancelled()) {
						break;
					}
					updateProgress(i, max);
					updateMessage(Constant.task_part + String.valueOf(i) + Constant.complete);

					Thread.sleep(100);
				}
				return null;
			}
		};

		longTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(final WorkerStateEvent t) {
				taskUpdateStage.hide();
			}
		});
		progress.progressProperty().bind(longTask.progressProperty());

		taskUpdateStage.show();
		new Thread(longTask).start();
	}

	/**
	 * generate Image From PDF
	 * 
	 * @param pdffile
	 * @return lfiles
	 */
	public List<File> generateImageFromPDF(File pdffile) {
		List<File> lfiles = new ArrayList<File>();
		File file = null;
		int min = 0;
		int max = 999999999;
		PDDocument document = null;
		try {
			document = PDDocument.load(new File(pdffile.getAbsolutePath()));
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			for (int i = 0; i < document.getPages().getCount(); i++) {
				// Save every page as PNG image
				BufferedImage img = pdfRenderer.renderImage(i);
				int random = (int) (Math.random() * (max - min + 1) + min);
				file = new File(pdffile.getParent() + "//" + String.format(("%d.png"), random));
				ImageIO.write(img, Constant.png, file);
				lfiles.add(file);
			}
			// Closing the PDF document
			document.close();
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			file.deleteOnExit();
		}
		return lfiles;
	}

	/**
	 * open html file in browser
	 * 
	 * @param file
	 */
	private void openFile(final File file) {
		try {
			this.desktop.open(file);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * print log
	 * 
	 * @param textArea
	 * @param files
	 */
	private void printLog(final TextField textArea, final List<File> files) {
		if (files == null || files.isEmpty()) {
			return;
		}
		for (final File file : files) {
			textArea.appendText(file.getAbsolutePath() + "\n");
		}
	}

	/**
	 * remove extension from file name
	 * 
	 * @param s
	 * @return string
	 */
	public static String removeExtension(final String s) {
		return s != null && s.lastIndexOf(".") > 0 ? s.substring(0, s.lastIndexOf(".")) : s;
	}

	/**
	 * start stage
	 * 
	 * @param stage
	 */
	@Override
	public void start(final Stage stage) {
		// Start ProgressBar creation
		final double wndwWidth = 150.0d;
		final double wndhHeigth = 150.0d;
		final ProgressIndicator progress = new ProgressIndicator();
		progress.setMinWidth(wndwWidth);
		progress.setMinHeight(wndhHeigth);
		progress.setProgress(0.25F);
		final VBox updatePane = new VBox();
		updatePane.setPadding(new Insets(10));
		updatePane.setSpacing(5.0d);
		updatePane.setAlignment(Pos.CENTER);
		updatePane.getChildren().addAll(progress);
		updatePane.setStyle("-fx-background-color: transparent");
		final Stage taskUpdateStage = new Stage(StageStyle.UNDECORATED);
		taskUpdateStage.setScene(new Scene(updatePane, 170, 170));
		// End progressBar
		// creating a text field
		textField = new TextField();
		// creating a text field
		textFieldxsl = new TextField();
		textField.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xml"));
		textField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("") || newValue == null) {
				// Tooltip xml
				textField.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xml"));
			}
		});
		textFieldxsl.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xsl"));
		textFieldxsl.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("") || newValue == null) {
				// Tooltip xml
				textFieldxsl.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xsl"));
			}
		});

		// filechooser
		final FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		final FileChooser fileChooserxsl = new FileChooser();
		fileChooserxsl.getExtensionFilters().add(new FileChooser.ExtensionFilter("XSL files (*.xsl)", "*.xsl"));

		// Creating the xml button
		xmlButton = I18N.buttonForKey("button.selectcda");
		final ImageView view = new ImageView(Constant.xmlphoto);
		xmlButton.setGraphic(view);
		xmlButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				Platform.runLater(() -> {
					textField.clear();
					final File file = fileChooser.showOpenDialog(stage);
					if (file != null) {
						final List<File> files = Arrays.asList(file);
						printLog(textField, files);
						// creating a constructor of file class and parsing an XML file
						final File fileXml = new File(file.getAbsolutePath());
						// an instance of factory that gives a document builder
						final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						// an instance of builder to parse the specified xml file
						DocumentBuilder db;
						try {
							db = dbf.newDocumentBuilder();
							org.w3c.dom.Document doc = db.parse(fileXml);
							doc.getDocumentElement().normalize();
							final NodeList nodeList = doc.getChildNodes();
							boolean trouve = false;
							for (int itr = 0; itr < nodeList.getLength(); itr++) {
								org.w3c.dom.Node node = nodeList.item(itr);
								if (node.getNodeName().equals(Constant.stylesheet)) {
									NodeList nList = doc.getElementsByTagName(Constant.stylesheet);
									for (int i = 0; i < nList.getLength(); i++) {
										org.w3c.dom.Node nNode = nList.item(i);
										org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
										if (eElement.getAttribute("xmlns:fo").length() > 0) {
											System.out.println("Attribute Exists");
											exist = true;
										} else {
											System.out.println("Attribute Does Not Exist");
											exist = false;
										}
									}
									isAutopresentable = true;
									textFieldxsl.setText(textField.getText());
									xslButton.setDisable(true);
									textFieldxsl.setDisable(true);
									trouve = true;
									break;
								} else {
									isAutopresentable = false;
								}

							}

							if (trouve == false) {
								textFieldxsl.clear();
								xslButton.setDisable(false);
								textFieldxsl.setDisable(false);
							}

							final Tooltip tooltip = new Tooltip();
							tooltip.setText(textField.getText());
							textField.setTooltip(tooltip);

						} catch (final ParserConfigurationException e) {
							e.printStackTrace();
						} catch (final SAXException e) {
							e.printStackTrace();
						} catch (final IOException e) {
							e.printStackTrace();
						}

					} else {
						// Tooltip xml
						textField.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xml"));
					}
				});
			}

		});

		// Creating the xml button
		xslButton = I18N.buttonForKey("button.selectxsl");
		ImageView viewxsl = new ImageView(Constant.xslphoto);
		xslButton.setGraphic(viewxsl);
		xslButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				textFieldxsl.clear();
				File file = fileChooserxsl.showOpenDialog(stage);
				if (file != null) {
					final List<File> files = Arrays.asList(file);
					printLog(textFieldxsl, files);
					// creating a constructor of file class and parsing an XML file
					final File fileXsl = new File(file.getAbsolutePath());
					// an instance of factory that gives a document builder
					final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					// an instance of builder to parse the specified xml file
					DocumentBuilder db;
					try {
						db = dbf.newDocumentBuilder();
						org.w3c.dom.Document doc = db.parse(fileXsl);
						doc.getDocumentElement().normalize();
						final NodeList nodeList = doc.getChildNodes();
						for (int itr = 0; itr < nodeList.getLength(); itr++) {
							org.w3c.dom.Node node = nodeList.item(itr);
							if (node.getNodeName().equals(Constant.stylesheet)) {
								NodeList nList = doc.getElementsByTagName(Constant.stylesheet);
								for (int i = 0; i < nList.getLength(); i++) {
									org.w3c.dom.Node nNode = nList.item(i);
									org.w3c.dom.Element eElement = (org.w3c.dom.Element) nNode;
									if (eElement.getAttribute("xmlns:fo").length() > 0) {
										System.out.println("Attribute Exists");
										exist = true;
									} else {
										System.out.println("Attribute Does Not Exist");
										exist = false;
									}
								}
								isAutopresentable = true;
							} else {
								isAutopresentable = false;
							}
						}
					} catch (final IOException e) {
						e.printStackTrace();
					} catch (final ParserConfigurationException e) {
						e.printStackTrace();
					} catch (final SAXException e) {
						e.printStackTrace();
					}

					// change tooltip textfield text
					final Tooltip tooltip = new Tooltip();
					tooltip.setText(textFieldxsl.getText());
					textFieldxsl.setTooltip(tooltip);
				} else {
					// Tooltip xsl
					textFieldxsl.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xsl"));
				}
			}
		});

		// Creating the stop button
		validateButton = I18N.buttonForKey("button.displaycda");
		ImageView viewdoc = new ImageView(Constant.docphoto);
		validateButton.setGraphic(viewdoc);
		validateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				try {
					if (!(textFieldxsl.getText().trim()).isEmpty() && !(textField.getText().trim()).isEmpty()
							&& ((textFieldxsl.getText().trim()).endsWith(Constant.xslext) || textFieldxsl.isDisabled())
							&& ((textField.getText().trim()).endsWith(Constant.xmlext)
									|| (textField.getText().trim()).endsWith(Constant.xmlext1))) {
						// call progress bar
						runTask(taskUpdateStage, progress);
						Platform.runLater(() -> {
							// create temp html file
							Path file1;
							List<File> lfiles = new ArrayList<File>();
							File file = null;
							byte[] decoder = null;
							Element image = null;
							String name = null;
							try {
								file1 = Files.createTempFile(null, Constant.htmlext);
								final String streamRes = transform(textFieldxsl.getText(), textField.getText());
								// Writes a string to the above temporary file
								Files.write(file1, streamRes.getBytes(StandardCharsets.UTF_8));

								final File url1 = new File(file1.toString());
								outputFileName = url1.getAbsolutePath();
								webEngine.load((url1.toURI()).toString());
								browser.prefHeightProperty().bind(stage.heightProperty());
								browser.prefWidthProperty().bind(stage.widthProperty());

								map.put(file1.toString(), textField.getText());
								mapXsl.put(file1.toString(), textFieldxsl.getText());

								// Start DATAMATRIX
								final InputStream targetStream = new FileInputStream(url1);
								final Document doc = Jsoup.parse(targetStream, Constant.utf8, "");
								// Get Element datamatrix for make some changes
								final Elements contents = doc.getElementsByClass("barcodeStyle");
								for (Element content : contents) {
									if (content != null) {
										final String dataMatrixValue = content.val();

										if (!dataMatrixValue.isEmpty()) {
											// Initialiser un objet de la classe BarcodeGenerator
											final BarcodeGenerator gen = new BarcodeGenerator(EncodeTypes.DATA_MATRIX,
													dataMatrixValue);

											// D�finir les pixels
											gen.getParameters().getBarcode().getXDimension().setPixels(4);

											// R�glez le mode d'encodage sur Auto
											gen.getParameters().getBarcode().getDataMatrix()
													.setDataMatrixEncodeMode(DataMatrixEncodeMode.AUTO);

											final Path matrix = Files.createTempFile(null, Constant.pngext);
											gen.save(matrix.toString(), BarCodeImageFormat.PNG);

											final Element img = new Element(Tag.valueOf(Constant.img), "")
													.attr(Constant.src, "file:///" + matrix.toString());
											content.appendChild(img);
											content.val("");
											content.attr("style", "padding:0px 35px;");
										}
									}
								}
								// e-prescription Matrix
								// Get Element datamatrix for make some changes
								final Element contentEprescription = doc.getElementById(Constant.elementMatrix);
								if (contentEprescription != null) {
									final String dataMatrixValueEprescription = contentEprescription.val();
									// Initialiser un objet de la classe BarcodeGenerator
									final BarcodeGenerator genEprescription = new BarcodeGenerator(
											EncodeTypes.DATA_MATRIX, dataMatrixValueEprescription);
									// D�finir les pixels
									genEprescription.getParameters().getBarcode().getXDimension().setPixels(4);
									// R�glez le mode d'encodage sur Auto
									genEprescription.getParameters().getBarcode().getDataMatrix()
											.setDataMatrixEncodeMode(DataMatrixEncodeMode.C40);

									final Path matrixEprescription = Files.createTempFile(null, Constant.pngext);
									genEprescription.save(matrixEprescription.toString(), BarCodeImageFormat.PNG);
									final Element imgEprescription = new Element(Tag.valueOf(Constant.img), "")
											.attr(Constant.src, "file:///" + matrixEprescription.toString());
									contentEprescription.appendChild(imgEprescription);

									contentEprescription.val("");
									contentEprescription.attr("style", "padding:0px 35px;");
								}
								// FIN DATAMATRIX
								// Start PDF
								final Elements iframes = doc.select("iframe,object");
								for (Element iframeChild : iframes) {
									if (iframeChild != null) {
//										final Element iframeChild = iframe.firstElementChild();
										final String source = iframeChild.attr(Constant.src);
										final String source1 = iframeChild.attr(Constant.data);
										final String find = Constant.b64;
										final boolean isFound = source.contains(Constant.b64);
										final boolean isFound1 = source1.contains(Constant.b64);
										int i = 0;
										String s1 = null;
										if (isFound) {
											i = source.indexOf(find);
											s1 = source.substring(i + 7);
										}
										if (isFound1) {
											i = source1.indexOf(find);
											s1 = source1.substring(i + 7);
										}
										if (isFound || isFound1) {
											if (i > 0) {
												if (s1.contains(Constant.spc1)) {
													s1 = s1.replaceAll(Constant.spc1, "");
												}
												if (s1.contains(Constant.spc2)) {
													s1 = s1.replaceAll(Constant.spc2, "");
												}
												final String b64 = s1.replaceAll(" ", "");
												file = File.createTempFile("tmp", Constant.pdfext);
												if (file.exists()) {
													file.delete();
												}
												file = File.createTempFile("tmp", Constant.pdfext);
												try (final FileOutputStream fos = new FileOutputStream(file);) {
													decoder = Base64.getDecoder().decode(b64);
													fos.write(decoder);
												} catch (final Exception e) {
													e.printStackTrace();
												}
												// PDF to Images
												lfiles = generateImageFromPDF(file);
												mergeImage(lfiles);
												String outputPath = file.getParent().concat("\\merged.png");
												if (!lfiles.isEmpty() && lfiles.size() != 0) {
													name = new File(outputPath).getAbsolutePath();
													image = new Element(Tag.valueOf(Constant.img), "")
															.attr(Constant.src, "file:///" + name);
													iframeChild.replaceWith(image);
												}
												// END PDF to Images
											}
										}
									}
								}
								// FIN PDF
								final String docHtml = doc.html();
								final PrintWriter printWriter = new PrintWriter(url1, Constant.utf8);
								printWriter.print("");
								printWriter.print(docHtml);
								printWriter.close();
								webEngine.load(new File(url1.getPath()).toURI().toURL().toString());
								menuBar.setDisable(false);
								mediator.setWebEngine(webEngine);
							} catch (final IOException e) {
								e.printStackTrace();
							} catch (final Exception e) {
								e.printStackTrace();
							} finally {
								if (file != null)
									file.deleteOnExit();
							}

						});
						// champs xml ou xsl vide --> error
					} else {
						final Alert alert = new Alert(AlertType.WARNING);
						final DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
						dialogPane.getStyleClass().add("myDialog");
						dialogPane.setMinHeight(130);
						dialogPane.setMaxHeight(130);
						dialogPane.setPrefHeight(130);
						alert.setContentText(I18N.getString("popup.error1"));
						alert.setHeaderText(null);
						alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(true);
						alert.showAndWait();
						menuBar.setDisable(true);
					}
				} catch (final Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		// Creating the xml button
		openButton = I18N.buttonForKey("button.opennav");
		ImageView viewopen = new ImageView(Constant.wwwphoto);
		openButton.setGraphic(viewopen);
		openButton.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				if (!outputFileName.isEmpty()) {
					final File url1 = new File(outputFileName);
					if (url1 != null) {
						// call open file method
						openFile(url1);
					}
				} else {
					final Alert alert = new Alert(AlertType.WARNING);
					final DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
					dialogPane.getStyleClass().add("myDialog");
					dialogPane.setMinHeight(140);
					dialogPane.setMaxHeight(140);
					dialogPane.setPrefHeight(140);
					alert.setContentText(I18N.getString("popup.error2"));
					alert.setHeaderText(null);
					alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(true);
					alert.showAndWait();
				}
			}
		});

		pdfButton = I18N.buttonForKey("button.generatepdf");
		ImageView viewpdf = new ImageView(Constant.pdfphoto);
		pdfButton.setGraphic(viewpdf);
		pdfButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				try {
					if (!(textFieldxsl.getText().trim()).isEmpty() && !(textField.getText().trim()).isEmpty()
							&& ((textFieldxsl.getText().trim()).endsWith(Constant.xslext) || textFieldxsl.isDisabled())
							&& ((textField.getText().trim()).endsWith(Constant.xmlext)
									|| (textField.getText().trim()).endsWith(Constant.xmlext1))) {
						Path pdf = Files.createTempFile(null, Constant.pdfext);
						final Path pdfFile = pdf;
						// call progress bar
						runTask(taskUpdateStage, progress);
						Platform.runLater(() -> {
							final String pathStr = textFieldxsl.getText();
							String path;
							if (isAutopresentable = false) {
								path = new File(pathStr).getParentFile().getAbsolutePath();
							} else {
								path = new File(pathStr).getParentFile().getParentFile().getAbsolutePath()
										.concat(Constant.path_fds);
							}
							String str = path + Constant.path_fop;
							// check fop folder if exist
							final Path pathFOPFolder = Paths.get(str);
							Path pathFOP;
							String xslPath = null;
							if (exist == false) {
								final Alert dialog = new Alert(AlertType.INFORMATION);
								final DialogPane dialogPane = dialog.getDialogPane();
								dialog.setHeaderText(null);
								dialog.setContentText(I18N.getString("popup.information"));
								dialogPane.setMinHeight(200);
								dialogPane.setMaxHeight(200);
								dialogPane.setPrefHeight(200);
								dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
								dialogPane.getStyleClass().add("myDialog");
								final ButtonType okButtonType = new ButtonType(I18N.getString("button.generatepdf"));
								dialog.getButtonTypes().setAll(okButtonType);
								Optional<ButtonType> result = dialog.showAndWait();
								if (result.get() == okButtonType) {

									final String namePDF = new File(textField.getText()).getName();
									final File basedir = new File(System.getProperty("java.io.tmpdir"));
									if (new File(basedir, removeExtension(namePDF) + Constant.pdfext).exists()) {
										new File(basedir, removeExtension(namePDF) + Constant.pdfext).delete();
									}
									File pdfFinal = new File(basedir, removeExtension(namePDF) + Constant.pdfext);
									File out = HTMLToPDF.main(textField.getText().trim(),
											textFieldxsl.getText().trim());
									if (out != null) {
										// rename if file exists
										int i = 1;
										while (pdfFinal.exists()) {
											final String path1 = pdfFinal.getAbsolutePath();
											final int idot = path1.lastIndexOf(".");
											pdfFinal = new File(
													path1.substring(0, idot) + "(" + ++i + ")" + path1.substring(idot));
										}
										ConvertPdfToPdfA.convert(out.toString(), pdfFinal.toString(), Constant.ans,
												Constant.viewer);
										// open pdf file
										openFile(new File(pdfFinal.toString()));
									}

								}
							} else {
								xslPath = textFieldxsl.getText();
								// externe fds xsl folder
								if (!Files.exists(pathFOPFolder)) {
									pathFOP = pathFOPFolder.getParent().getParent().getParent().getParent();
									str = pathFOP.toFile() + Constant.path_fop;
									// autoprésentable xsl folder
									if (!Files.exists(Paths.get(str))) {
										str = pathFOP.toFile().getAbsolutePath() + Constant.path_fds
												+ Constant.path_fop;
									}
								}

								if (Files.exists(Paths.get(str))) {
									String cmd = "cd " + '"' + str + '"' + " && fop -xml " + '"' + textField.getText()
											+ '"' + " -xsl " + '"' + xslPath + '"' + " -pdf " + '"' + pdfFile.toString()
											+ '"';
									cmd = cmd.replace("\\", "/");
									final String[] command = new String[3];
									command[0] = Constant.cmd;
									command[1] = Constant.c_slash;
									command[2] = cmd;
									final ProcessBuilder pb = new ProcessBuilder();
									pb.command(command);
									Process process;
									try {
										process = pb.start();
										final BufferedReader errStreamReader = new BufferedReader(
												new InputStreamReader(process.getErrorStream()));

										String line = errStreamReader.readLine();
										while (line != null) {
											line = errStreamReader.readLine();
										}
										final String namePDF = new File(textField.getText()).getName();
										final File basedir = new File(System.getProperty("java.io.tmpdir"));
										if (new File(basedir, removeExtension(namePDF) + Constant.pdfext).exists()) {
											new File(basedir, removeExtension(namePDF) + Constant.pdfext).delete();
										}
										File pdfFinal = new File(basedir, removeExtension(namePDF) + Constant.pdfext);
										if (pdfFile != null) {
											// rename if file exists
											int i = 1;
											while (pdfFinal.exists()) {
												final String path1 = pdfFinal.getAbsolutePath();
												final int idot = path1.lastIndexOf(".");
												pdfFinal = new File(path1.substring(0, idot) + "(" + ++i + ")"
														+ path1.substring(idot));
											}
											ConvertPdfToPdfA.convert(pdfFile.toString(), pdfFinal.toString(),
													Constant.ans, Constant.viewer);
											// open pdf file
											openFile(new File(pdfFinal.toString()));
										}
									} catch (final IOException e) {
										e.printStackTrace();
									} catch (final Exception e) {
										e.printStackTrace();
									}
								}
							}
						});
					} else {
						final Alert alert = new Alert(AlertType.WARNING);
						final DialogPane dialogPane = alert.getDialogPane();
						dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
						dialogPane.getStyleClass().add("myDialog");
						dialogPane.setMinHeight(130);
						dialogPane.setMaxHeight(130);
						dialogPane.setPrefHeight(130);
						alert.setContentText(I18N.getString("popup.error1"));
						alert.setHeaderText(null);
						alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(true);
						alert.showAndWait();
					}
				} catch (final IOException e) {
					e.printStackTrace();
				}
			}
		});

		showXmlButton = I18N.buttonForKey("button.showcda");
		ImageView viewxml = new ImageView(Constant.openphoto);
		showXmlButton.setGraphic(viewxml);
		showXmlButton.setOnAction(e -> {
			Platform.setImplicitExit(false);
			Font.loadFont(this.getClass().getResourceAsStream("/Roboto-Regular.ttf"), 16);

			FXMLLoader loader = null;
			try {
				if (!(textField.getText().trim()).isEmpty() && ((textField.getText().trim()).endsWith(Constant.xmlext)
						|| (textField.getText().trim()).endsWith(Constant.xmlext1))) {
					final URL url = getClass().getResource("/main.fxml");
					loader = new FXMLLoader(url);
					final Parent root = loader.load();
					Platform.runLater(() -> {
						final Scene scene = new Scene(root);
						// stylesheet css
						scene.getStylesheets().add(WebViewSample.class.getResource("/style.css").toExternalForm());
						primaryStage = new Stage();
						primaryStage.setScene(scene);
						primaryStage.setMinHeight(640);
						primaryStage.setMinWidth(640);
						primaryStage.setTitle(textField.getText());
						primaryStage.setMaximized(true);
						primaryStage.setOnShowing(event -> {
							final String xmlStr = getTextField().getText();
							final File file = new File(xmlStr);
							if (file != null) {
								MainMenuBar mainMenuBar = new MainMenuBar();
								mainMenuBar.readFile(file, textFieldxsl.getText());
							}
						});
						primaryStage.setOnCloseRequest(event -> {
							EditorUtils.onCloseExitConfirmation(primaryStage);
							event.consume();
							MainMenuBar bar = new MainMenuBar();
							if (bar.getSecondStage() != null) {
								if (bar.getSecondStage().isShowing()) {
									bar.getSecondStage().close();
								}
							}
							if (MainMenuBar.getStage() != null) {
								if (MainMenuBar.getStage().isShowing()) {
									MainMenuBar.getStage().close();
								}
							}
						});
						primaryStage.show();
					});
				} else {
					final Alert alert = new Alert(AlertType.WARNING);
					final DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
					dialogPane.getStyleClass().add("myDialog");
					dialogPane.setMinHeight(130);
					dialogPane.setMaxHeight(130);
					dialogPane.setPrefHeight(130);
					alert.setContentText(I18N.getString("popup.error3"));
					alert.setHeaderText(null);
					alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(true);
					alert.showAndWait();
				}
			} catch (final IOException e1) {
				e1.printStackTrace();
			}
		});

		clearButton = I18N.buttonForKey("button.initinterface");

		ImageView viewclear = new ImageView(Constant.clearphoto);
		clearButton.setGraphic(viewclear);
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				webEngine.loadContent("");
				textField.setText("");
				textFieldxsl.setText("");
				outputFileName = "";
				xslButton.setDisable(false);
				textFieldxsl.setDisable(false);
				menuBar.setDisable(true);
				// Tooltip xml
				textField.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xml"));
				// Tooltip xsl
				textFieldxsl.setTooltip(I18N.createBoundTooltip("textfield.tooltip.xsl"));

				final Alert alert = new Alert(AlertType.INFORMATION);
				final DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
				dialogPane.getStyleClass().add("myDialog");
				dialogPane.setMinHeight(130);
				dialogPane.setMaxHeight(130);
				dialogPane.setPrefHeight(130);
				alert.setContentText(I18N.getString("popup.error5"));
				alert.setHeaderText(null);
				alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(false);
				final Timeline idlestage = new Timeline(
						new KeyFrame(Duration.seconds(2), new EventHandler<ActionEvent>() {
							@Override
							public void handle(final ActionEvent event) {
								alert.setResult(ButtonType.CANCEL);
								alert.hide();
							}
						}));
				idlestage.setCycleCount(1);
				idlestage.play();
				final Optional<ButtonType> result = alert.showAndWait();
				if (result.get() != null) {
					if (result.get() == ButtonType.OK) {
						System.out.println("ok clicked");
					} else if (result.get() == ButtonType.CANCEL) {
						System.out.println("cancel clicked");
					}
				}
				// close xml editor
				if (primaryStage != null) {
					if (primaryStage.isShowing()) {
						primaryStage.close();
					}
				}
			}
		});

		openWithButton = I18N.buttonForKey("button.openwith");
		ImageView viewOpen = new ImageView(Constant.oxygenphoto);
		openWithButton.setGraphic(viewOpen);
		openWithButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {

				if (!textField.getText().isEmpty() && ((textField.getText().trim()).endsWith(Constant.xmlext)
						|| (textField.getText().trim()).endsWith(Constant.xmlext1))) {
					String cmd = textField.getText();
					cmd = cmd.replace("\\", "/");
					final String[] command = new String[3];
					command[0] = Constant.cmd;
					command[1] = Constant.c_slash;
					command[2] = cmd;
					final ProcessBuilder pb = new ProcessBuilder();
					pb.command(command);

					try {
						final Process process = pb.start();
						final BufferedReader errStreamReader = new BufferedReader(
								new InputStreamReader(process.getErrorStream()));

						String line = errStreamReader.readLine();
						while (line != null) {
							line = errStreamReader.readLine();
						}
					} catch (final IOException e) {
						e.printStackTrace();
					}
				} else {
					final Alert alert = new Alert(AlertType.WARNING);
					final DialogPane dialogPane = alert.getDialogPane();
					dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
					dialogPane.getStyleClass().add("myDialog");
					dialogPane.setMinHeight(130);
					dialogPane.setMaxHeight(130);
					dialogPane.setPrefHeight(130);
					alert.setContentText(I18N.getString("popup.error3"));
					alert.setHeaderText(null);
					alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(true);
					alert.showAndWait();
				}
			}
		});

		final HBox hBox = new HBox(10);
		// retrieving the observable list of the HBox
		final ObservableList<Node> listH = hBox.getChildren();
		// English language
		final BackgroundImage backgroundImage = new BackgroundImage(
				new Image(WebViewSample.class.getResource("/en.png").toExternalForm()), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		final Background background = new Background(backgroundImage);
		final Button buttonEnglish = I18N.buttonForKey("button.english");
		buttonEnglish.setBackground(background);
		buttonEnglish.setPrefWidth(30);
		buttonEnglish.setPrefHeight(20);
		buttonEnglish.setMinHeight(20);
		buttonEnglish.setMaxHeight(20);
		buttonEnglish.setStyle("-fx-border-color: #98bb68;");
		// Tooltip English flag
		buttonEnglish.setTooltip(I18N.createBoundTooltip("button.tooltip.english"));
		buttonEnglish.setOnAction((evt) -> switchLanguage(Locale.ENGLISH));

		// French language
		final BackgroundImage backgroundImageFr = new BackgroundImage(
				new Image(WebViewSample.class.getResource("/fr.png").toExternalForm()), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		final Background backgroundFr = new Background(backgroundImageFr);
		final Button buttonFrensh = I18N.buttonForKey("button.frensh");
		buttonFrensh.setBackground(backgroundFr);
		buttonFrensh.setPrefWidth(30);
		buttonFrensh.setPrefHeight(20);
		buttonFrensh.setMinHeight(20);
		buttonFrensh.setMaxHeight(20);
		buttonFrensh.setStyle("-fx-border-color: #98bb68;");
		// Tooltip Frensh flag
		buttonFrensh.setTooltip(I18N.createBoundTooltip("button.tooltip.frensh"));
		buttonFrensh.setOnAction((evt) -> switchLanguage(Locale.FRENCH));

		// Deutch language
		final BackgroundImage backgroundImageDe = new BackgroundImage(
				new Image(WebViewSample.class.getResource("/de.png").toExternalForm()), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		final Background backgroundDe = new Background(backgroundImageDe);
		final Button buttonDeutch = I18N.buttonForKey("button.deutch");
		buttonDeutch.setBackground(backgroundDe);
		buttonDeutch.setPrefWidth(30);
		buttonDeutch.setPrefHeight(20);
		buttonDeutch.setMinHeight(20);
		buttonDeutch.setMaxHeight(20);
		buttonDeutch.setStyle("-fx-border-color: #98bb68;");
		// Tooltip Deutch flag
		buttonDeutch.setTooltip(I18N.createBoundTooltip("button.tooltip.deutch"));
		buttonDeutch.setOnAction((evt) -> switchLanguage(Locale.GERMAN));

		// Netherland language
		final BackgroundImage backgroundImageNl = new BackgroundImage(
				new Image(WebViewSample.class.getResource("/nl.png").toExternalForm()), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		final Background backgroundNl = new Background(backgroundImageNl);
		final Button buttonNl = I18N.buttonForKey("button.nl");
		buttonNl.setBackground(backgroundNl);
		buttonNl.setPrefWidth(30);
		buttonNl.setPrefHeight(20);
		buttonNl.setMinHeight(20);
		buttonNl.setMaxHeight(20);
		buttonNl.setStyle("-fx-border-color: #98bb68;");
		// Tooltip Deutch flag
		buttonNl.setTooltip(I18N.createBoundTooltip("button.tooltip.nl"));
		buttonNl.setOnAction((evt) -> switchLanguage(Locale.forLanguageTag("nl-NL")));

		// Espagnol language
		final BackgroundImage backgroundImageEs = new BackgroundImage(
				new Image(WebViewSample.class.getResource("/es.png").toExternalForm()), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		final Background backgroundEs = new Background(backgroundImageEs);
		final Button buttonEs = I18N.buttonForKey("button.espagnol");
		buttonEs.setBackground(backgroundEs);
		buttonEs.setPrefWidth(30);
		buttonEs.setPrefHeight(20);
		buttonEs.setMinHeight(20);
		buttonEs.setMaxHeight(20);
		buttonEs.setStyle("-fx-border-color: #98bb68;");
		// Tooltip Deutch flag
		buttonEs.setTooltip(I18N.createBoundTooltip("button.tooltip.espagnol"));
		buttonEs.setOnAction((evt) -> switchLanguage(Locale.forLanguageTag("es-ES")));

		// Portugal language
		final BackgroundImage backgroundImagePt = new BackgroundImage(
				new Image(WebViewSample.class.getResource("/pt.png").toExternalForm()), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		final Background backgroundPt = new Background(backgroundImagePt);
		final Button buttonPt = I18N.buttonForKey("button.portugais");
		buttonPt.setBackground(backgroundPt);
		buttonPt.setPrefWidth(30);
		buttonPt.setPrefHeight(20);
		buttonPt.setMinHeight(20);
		buttonPt.setMaxHeight(20);
		buttonPt.setStyle("-fx-border-color: #98bb68;");
		// Tooltip Deutch flag
		buttonPt.setTooltip(I18N.createBoundTooltip("button.tooltip.portugais"));
		buttonPt.setOnAction((evt) -> switchLanguage(Locale.forLanguageTag("pt-PT")));

		// Italian language
		final BackgroundImage backgroundImageIt = new BackgroundImage(
				new Image(WebViewSample.class.getResource("/it.png").toExternalForm()), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(1.0, 1.0, true, true, false, false));
		final Background backgroundIt = new Background(backgroundImageIt);
		final Button buttonIt = I18N.buttonForKey("button.italian");
		buttonIt.setBackground(backgroundIt);
		buttonIt.setPrefWidth(30);
		buttonIt.setPrefHeight(20);
		buttonIt.setMinHeight(20);
		buttonIt.setMaxHeight(20);
		buttonIt.setStyle("-fx-border-color: #98bb68;");
		// Tooltip Deutch flag
		buttonIt.setTooltip(I18N.createBoundTooltip("button.tooltip.italian"));
		buttonIt.setOnAction((evt) -> switchLanguage(Locale.ITALIAN));

		final Image ansImage = new Image(WebViewSample.class.getResource("/ans01.jpg").toExternalForm());
		// creating ImageView for adding image
		final ImageView imageView = new ImageView();
		imageView.setImage(ansImage);
		imageView.setFitWidth(100);
		imageView.setFitHeight(100);
		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);

		// creating HBox to add imageview
		final HBox hBoxImg = new HBox();
		hBoxImg.getChildren().addAll(imageView);
		hBoxImg.setStyle("-fx-background-color: white;");

		// space region between button and flags
		final Region spacer10 = new Region();
		spacer10.setMaxWidth(1000);
		HBox.setHgrow(spacer10, Priority.ALWAYS);

		final HBox hBoxint = new HBox();
		// retrieving the observable list of the HBox
		final ObservableList<Node> listHint = hBoxint.getChildren();
		hBoxint.setStyle("-fx-background-color: gray");
		// Adding all the nodes to the observable list
		listHint.addAll(spacer10, buttonFrensh, buttonEnglish, buttonDeutch, buttonNl, buttonEs, buttonPt, buttonIt);

		// Adding all the nodes to the observable list
		listH.addAll(xmlButton, textField, xslButton, textFieldxsl);

		final HBox hBoxFlag = new HBox(10);

		// retrieving the observable list of the HBox
		final ObservableList<Node> listHFlag = hBoxFlag.getChildren();
		listHFlag.addAll(listHint);

		final HBox hBoxOne = new HBox(20);
		// retrieving the observable list of the Hbox
		final ObservableList<Node> listHOne = hBoxOne.getChildren();
		// Adding all the nodes to the observable list
		listHOne.addAll(validateButton, openButton, pdfButton, showXmlButton, openWithButton, clearButton);

		final VBox vBoxAll = new VBox(5);
		final ObservableList<Node> listVBoxAll = vBoxAll.getChildren();
		listVBoxAll.addAll(hBoxFlag, hBox, hBoxOne);

		final SplitPane sp = new SplitPane();
		sp.setStyle("-fx-box-border: 0px;");
		sp.setOrientation(Orientation.HORIZONTAL);
		sp.setDividerPositions(0.01f, 0.9f);
		sp.getItems().addAll(hBoxImg, vBoxAll);

		// Instantiating the VBox class
		final VBox vBox = new VBox(10);
		vBox.setStyle("-fx-background-color: transparent;");

		final File url1 = new File(outputFileName);
		try {
			webEngine.load(new File(url1.getPath()).toURI().toURL().toString());
			browser.prefHeightProperty().bind(stage.heightProperty());
			browser.prefWidthProperty().bind(stage.widthProperty());

			// A line in Ox Axis
			Separator oxLine1 = new Separator();
			oxLine1.setOrientation(Orientation.HORIZONTAL);

			// Stroke Width
			Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
			// width will store the width of the screen
			int width = (int) size.getWidth();
			oxLine1.setMaxWidth(width - 10);
			oxLine1.setStyle("-fx-background-color: black;  -fx-pref-height: 1;");

			final Menu menu = I18N.menuForKey("menu.file");
			final MenuItem menuItem1 = I18N.menuBarForKey("menu.print");
			final MenuItem menuItem2 = I18N.menuBarForKey("button.copy.text");

			menu.getItems().addAll(menuItem2, menuItem1);

			final Menu menu1 = I18N.menuForKey("menu.display");

			final Menu subMenu = I18N.menuForKey("menu.zoom");

			final MenuItem menuItem11 = I18N.menuBarForKey("menu.zoomavant");
			subMenu.getItems().add(menuItem11);

			final MenuItem menuItem12 = I18N.menuBarForKey("menu.zoomarriere");
			subMenu.getItems().add(menuItem12);

			final MenuItem menuItem13 = I18N.menuBarForKey("menu.taille");
			subMenu.getItems().add(menuItem13);
			menu.getItems().add(subMenu);

			final MenuItem menuItem4 = I18N.menuBarForKey("menu.pleinecran");

			menu1.getItems().add(subMenu);
			menu1.getItems().add(menuItem4);

			final Menu menu2 = I18N.menuForKey("menu.help");
			final MenuItem menuItem6 = I18N.menuBarForKey("menu.apropos");

			menu2.getItems().add(menuItem6);

			final Menu menu3 = I18N.menuForKey("menu.history");
			final MenuItem menuItem7 = I18N.menuBarForKey("menu.display.history");

			menu3.getItems().add(menuItem7);

			menuBar.getMenus().addAll(menu, menu1, menu3, menu2);
			menuBar.setDisable(true);

			menuItem2.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));

			menuItem11.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));

			menuItem12.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));

			menuItem13.setAccelerator(new KeyCodeCombination(KeyCode.F10));

			menuItem4.setAccelerator(new KeyCodeCombination(KeyCode.F11));

			menuItem7.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));

			final HBox boxCombo = new HBox();
			boxCombo.setVisible(false);
			final HBox controls = new HBox(10);
			final Label label = new Label();
			ComboBox<WebHistory.Entry> comboBox = new ComboBox<WebHistory.Entry>();
			label.setText(I18N.getString("menu.history") + " : ");
			label.setPadding(new Insets(5, 0, 0, 20));
			label.setFont(Font.font(14));
			boxCombo.getChildren().addAll(label, comboBox);
			comboBox.getSelectionModel().clearSelection();
			comboBox.getItems().clear();

			menuItem7.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					controls.setVisible(false);
					// Set a cell factory to to show only the page title in the history list
					comboBox.setCellFactory(new Callback<ListView<WebHistory.Entry>, ListCell<WebHistory.Entry>>() {
						@Override
						public ListCell<WebHistory.Entry> call(ListView<WebHistory.Entry> list) {
							final ListCell<Entry> cell = new ListCell<Entry>() {
								@Override
								public void updateItem(final Entry item, final boolean empty) {
									super.updateItem(item, empty);

									if (empty) {
										this.setText(null);
										this.setGraphic(null);
									} else {
										final String pageTitle = item.getTitle();
										if (pageTitle != null) {
											final String[] words = pageTitle.split("<");
											this.setText(words[0]);
										}
									}
								}
							};

							return cell;
						}
					});

					comboBox.setPadding(new Insets(5, 0, 0, 20));
					comboBox.setMinWidth(500);
					comboBox.setMaxWidth(500);
					comboBox.setPrefWidth(500);
					boxCombo.setMaxWidth(Control.USE_PREF_SIZE);
					boxCombo.setVisible(true);
					comboBox.setItems(history.getEntries());
					comboBox.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(final ActionEvent ev) {
							final int offset = comboBox.getSelectionModel().getSelectedIndex()
									- history.getCurrentIndex();
							history.go(offset);
							boxCombo.setVisible(false);
						}
					});

					history.currentIndexProperty().addListener(new ChangeListener<Number>() {

						@Override
						public void changed(final ObservableValue<? extends Number> observable, Number oldValue,
								Number newValue) {
							// update currently selected combobox item
							comboBox.getSelectionModel().select(newValue.intValue());

							final String pagePath = comboBox.getSelectionModel().getSelectedItem().getUrl();
							if (pagePath != null) {
								final String[] word = pagePath.split("file:/");
								final String mapWord = word[1];
								final File file1 = new File(mapWord);
								for (Map.Entry<String, String> mapentry : map.entrySet()) {
									final File file2 = new File((String) mapentry.getKey());
									if (file1.compareTo(file2) == 0) {
										textField.setText((String) mapentry.getValue());
										if (textField.getText().contains(Constant.presentable)
												|| textField.getText().contains(Constant.ppresentable)) {
											textFieldxsl.setDisable(true);
											xslButton.setDisable(true);
										} else {
											textFieldxsl.setDisable(false);
											xslButton.setDisable(false);
										}
										break;
									}
								}
								for (Map.Entry<String, String> mapentry : mapXsl.entrySet()) {
									final File file2 = new File((String) mapentry.getKey());
									if (file1.compareTo(file2) == 0) {
										textFieldxsl.setText((String) mapentry.getValue());
										break;
									}
								}
							}
							boxCombo.setVisible(false);
						}

					});

					// set converter for value shown in the combobox:
					comboBox.setConverter(new StringConverter<WebHistory.Entry>() {

						@Override
						public String toString(final WebHistory.Entry object) {
							return object == null ? null : object.getUrl();
						}

						@Override
						public WebHistory.Entry fromString(final String string) {
							throw new UnsupportedOperationException();
						}
					});
					comboBox.getSelectionModel().clearSelection();
				}

			});

			menuItem6.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					Platform.runLater(() -> {
						final Stage stageT = new Stage(StageStyle.UTILITY);
						// Setting the text to be added.
						final String apropos = I18N.getString("menu.title.propo");
						final Label apropsT = new Label(apropos);
						final String version = "1.0.0";
						final Label versionT = new Label(version);
						final String browser = I18N.getString("menu.browser");
						final Label browserT = new Label(browser);

						apropsT.setFont(Font.font(20));
						apropsT.setStyle("-fx-font-weight: bold");
						apropsT.setMaxWidth(Double.MAX_VALUE);
						AnchorPane.setLeftAnchor(apropsT, 0.0);
						AnchorPane.setRightAnchor(apropsT, 0.0);
						apropsT.setAlignment(Pos.BASELINE_LEFT);
						apropsT.setPadding(new Insets(10, 0, 0, 20));

						versionT.setFont(Font.font(14));
						versionT.setMaxWidth(Double.MAX_VALUE);
						AnchorPane.setLeftAnchor(versionT, 0.0);
						AnchorPane.setRightAnchor(versionT, 0.0);
						versionT.setAlignment(Pos.BASELINE_LEFT);
						versionT.setPadding(new Insets(10, 0, 0, 20));

						browserT.setPadding(new Insets(10, 0, 0, 20));
						browserT.setFont(Font.font(14));
						browserT.setTextAlignment(TextAlignment.LEFT);
						browserT.setWrapText(true);
						browserT.setMaxWidth(250);

						final FlowPane root = new FlowPane();
						root.setPadding(new Insets(10));
						root.setHgap(10);
						root.setVgap(10);
						final VBox vbox = new VBox();
						vbox.setStyle("-fx-background-color: #e9e4e6;");
						vbox.getChildren().addAll(apropsT, versionT, browserT);
						root.getChildren().add(vbox);
						root.setStyle("-fx-background-color: #e9e4e6;");

						// Creating a scene object
						final Scene sceneT = new Scene(root, 300, 200);
						// Setting title to the Stage
						stageT.titleProperty().bind(I18N.createStringBinding("menu.apropos"));
						// Adding scene to the stage
						stageT.setScene(sceneT);
						// Specifies the modality for new window.
						stageT.initModality(Modality.WINDOW_MODAL);
						// Specifies the owner Window (parent) for new window
						stageT.initOwner(stage);
						stageT.setResizable(false);
						// Displaying the contents of the stage
						stageT.show();
					});
				}
			});

			menuItem4.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					stage.setFullScreen(true);
				}
			});

			menuItem2.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					Clipboard clipboard = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					final String selection = (String) browser.getEngine()
							.executeScript("window.getSelection().toString()");
					content.putString(selection);
					clipboard.setContent(content);
				}
			});

			menuItem12.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					final Double zoom = browser.getZoom();
					browser.setZoom(zoom - 0.25);
				}
			});

			menuItem11.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					final Double zoom = browser.getZoom();
					browser.setZoom(zoom + 0.25);
				}
			});

			menuItem13.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					browser.setZoom(1);
				}
			});

			final HBox allHbox = new HBox(10);
			controls.setMinHeight(Control.USE_PREF_SIZE);
			controls.setMaxWidth(Control.USE_PREF_SIZE);
			controls.disableProperty().bind(browser.getEngine().getLoadWorker().runningProperty());
			controls.setVisible(false);

			menuItem1.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));

			TextField searchBar = new TextField();

			EventHandler<ActionEvent> handler = event -> {
				controls.setVisible(false);
				searchBar.setText("");

			};
			buttonX.setOnAction(handler);

			final Region spacer = new Region();
			spacer.setMaxWidth(20);
			HBox.setHgrow(spacer, Priority.ALWAYS);

			controls.getChildren().addAll(spacer, searchBar, button, buttonX);

			Field pageField = browser.getClass().getDeclaredField("page");
			pageField.setAccessible(true);
			WebPage page = (com.sun.webkit.WebPage) pageField.get(browser);
			button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					try {
						page.find(searchBar.getText(), true, true, false);
					} catch (final IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			});

			menuItem1.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent event) {
					boxCombo.setVisible(false);
					controls.setVisible(true);
				}
			});

			final VBox hBox2 = new VBox();
			hBox2.setMaxWidth(Integer.MAX_VALUE);
			final ObservableList<Node> listHB = hBox2.getChildren();

			allHbox.getChildren().addAll(controls, boxCombo);

			listHB.addAll(oxLine1, menuBar, allHbox, browser);

			// retrieving the observable list of the VBox
			final ObservableList<Node> list = vBox.getChildren();
			// Adding all the nodes to the observable list
			list.addAll(sp, hBox2);

			// Creating a scene object
			final Scene scene = new Scene(vBox);

			stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(Constant.photo)));
			// Setting title to the Stage
			// stage.setTitle(Constant.generate_cda);
			stage.titleProperty().bind(I18N.createStringBinding("window.title"));
			// Adding scene to the stage
			stage.setScene(scene);
			stage.setMaxWidth(Integer.MAX_VALUE);
			stage.setMaxHeight(Integer.MAX_VALUE);
			stage.setMaximized(true);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(final WindowEvent e) {
					Platform.exit();
					System.exit(0);
				}
			});

			scene.widthProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
						Number newSceneWidth) {
					if (newSceneWidth.doubleValue() >= 1509 && newSceneWidth.doubleValue() < 1632) { // ecran 16 pouces
						xmlButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						xslButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						validateButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						openButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						openWithButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						pdfButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						showXmlButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						clearButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						textFieldxsl.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						textField.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						button.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");
						buttonX.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 14; -fx-font-family: Verdana, Tahoma, sans-serif;");

						menuItem1.setStyle("-fx-font-size: 14;");
						menuItem2.setStyle("-fx-font-size: 14;");
						menu.setStyle("-fx-font-size: 14;");
						menu1.setStyle("-fx-font-size: 14;");
						subMenu.setStyle("-fx-font-size: 14;");
						menuItem11.setStyle("-fx-font-size: 14;");
						menuItem12.setStyle("-fx-font-size: 14;");
						menuItem13.setStyle("-fx-font-size: 14;");
						menuItem4.setStyle("-fx-font-size: 14;");
						menu2.setStyle("-fx-font-size: 14;");
						menuItem6.setStyle("-fx-font-size: 14;");
						menuBar.setStyle("-fx-font-size: 14;");

						textField.setPrefWidth(300);
						textField.setMinWidth(300);
						textField.setMaxWidth(300);
						textFieldxsl.setPrefWidth(300);
						textFieldxsl.setMinWidth(300);
						textFieldxsl.setMaxWidth(300);
						// hide webview scrollbars whenever they appear.
						browser.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
							@Override
							public void onChanged(Change<? extends Node> change) {
								Set<Node> nodes = browser.lookupAll(".scroll-bar");
								for (final Node node : nodes) {
									if (node instanceof ScrollBar) {
										final ScrollBar sb = (ScrollBar) node;
										if (sb.getOrientation() == Orientation.HORIZONTAL) {
											sb.setVisible(false);
										}
									}
								}
							}
						});
					}
					if (newSceneWidth.doubleValue() >= 1632 && newSceneWidth.doubleValue() >= 1728) { // ecran 17 pouces
						xmlButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						xslButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						validateButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						openButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						openWithButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						pdfButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						showXmlButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						clearButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						textFieldxsl.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						textField.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						button.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");
						buttonX.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 16; -fx-font-family: Verdana, Tahoma, sans-serif;");

						menuItem1.setStyle("-fx-font-size: 16;");
						menuItem2.setStyle("-fx-font-size: 16;");
						menu.setStyle("-fx-font-size: 16;");
						menu1.setStyle("-fx-font-size: 16;");
						subMenu.setStyle("-fx-font-size: 16;");
						menuItem11.setStyle("-fx-font-size: 16;");
						menuItem12.setStyle("-fx-font-size: 16;");
						menuItem13.setStyle("-fx-font-size: 16;");
						menuItem4.setStyle("-fx-font-size: 16;");
						menu2.setStyle("-fx-font-size: 16;");
						menuItem6.setStyle("-fx-font-size: 16;");
						menuBar.setStyle("-fx-font-size: 16;");

						textField.setPrefWidth(400);
						textField.setMinWidth(400);
						textField.setMaxWidth(400);
						textFieldxsl.setPrefWidth(400);
						textFieldxsl.setMinWidth(400);
						textFieldxsl.setMaxWidth(400);
						// hide webview scrollbars whenever they appear.
						browser.getChildrenUnmodifiable().addListener(new ListChangeListener<Node>() {
							@Override
							public void onChanged(Change<? extends Node> change) {
								Set<Node> nodes = browser.lookupAll(".scroll-bar");
								for (final Node node : nodes) {
									if (node instanceof ScrollBar) {
										final ScrollBar sb = (ScrollBar) node;
										if (sb.getOrientation() == Orientation.HORIZONTAL) {
											sb.setVisible(false);
										}
									}
								}
							}
						});

					} else if (newSceneWidth.doubleValue() < 1509) {
						xmlButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						xslButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						validateButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						openButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						openWithButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						pdfButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						showXmlButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						clearButton.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						textFieldxsl.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						textField.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5; -fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						button.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");
						buttonX.setStyle(
								"-fx-background-color: white; -fx-border-color: #98bb68; -fx-border-radius: 5;-fx-font-size: 10; -fx-font-family: Verdana, Tahoma, sans-serif;");

						menuItem1.setStyle("-fx-font-size: 10;");
						menuItem2.setStyle("-fx-font-size: 10;");
						menu.setStyle("-fx-font-size: 10;");
						menu1.setStyle("-fx-font-size: 10;");
						subMenu.setStyle("-fx-font-size: 10;");
						menuItem11.setStyle("-fx-font-size: 10;");
						menuItem12.setStyle("-fx-font-size: 10;");
						menuItem13.setStyle("-fx-font-size: 10;");
						menuItem4.setStyle("-fx-font-size: 10;");
						menu2.setStyle("-fx-font-size: 10;");
						menuItem6.setStyle("-fx-font-size: 10;");
						menuBar.setStyle("-fx-font-size: 10;");

						textField.setPrefWidth(280);
						textField.setMinWidth(280);
						textField.setMaxWidth(280);
						textFieldxsl.setPrefWidth(280);
						textFieldxsl.setMinWidth(280);
						textFieldxsl.setMaxWidth(280);

					}
					// Resize Button
					hBox.setMinWidth(stage.getWidth() - 120);
					hBox.setMaxWidth(stage.getWidth() - 120);
					hBox.setPrefWidth(stage.getWidth() - 120);
					hBoxOne.setMinWidth(stage.getWidth() - 100);
					hBoxOne.setMaxWidth(stage.getWidth() - 100);
					hBoxOne.setPrefWidth(stage.getWidth() - 100);
					hBoxFlag.setMinWidth(stage.getWidth() - 125);
					hBoxFlag.setMaxWidth(stage.getWidth() - 125);
					hBoxFlag.setPrefWidth(stage.getWidth() - 125);
					// align center hBoxOne
					hBoxFlag.setAlignment(Pos.BASELINE_CENTER);
					hBox.setAlignment(Pos.BASELINE_LEFT);
					hBoxOne.setAlignment(Pos.BASELINE_LEFT);
				}
			});

			// Displaying the contents of the stage
			stage.show();

			browser.requestFocus();

		} catch (final MalformedURLException e) {
			e.printStackTrace();
		} catch (final NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (final SecurityException e1) {
			e1.printStackTrace();
		} catch (final IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (final IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * @return the textField
	 */
	public TextField getTextField() {
		return textField;
	}

	/**
	 * @param textField the textField to set
	 */
	public void setTextField(final TextField textField) {
		this.textField = textField;
	}

	/**
	 * sets the given Locale in the I18N class and keeps count of the number of
	 * switches.
	 *
	 * @param locale the new local to set
	 */
	private void switchLanguage(final Locale locale) {
		numSwitches++;
		I18N.setLocale(locale);
	}

	/**
	 * merge images file
	 * @param lfiles
	 */
	private void mergeImage(List<File> lfiles) {
		int rows = lfiles.size(); // we assume the no. of rows and cols are known and each chunk has equal width
						// and height
		int cols = 1;
		int chunks = rows * cols;

		int chunkWidth, chunkHeight;
		int type;
		// fetching image files
		File[] imgFiles = new File[chunks];
		for (int i = 0; i < chunks; i++) {
			imgFiles[i] = new File(lfiles.get(i).getAbsolutePath());
		}

		// creating a bufferd image array from image files
		BufferedImage[] buffImages = new BufferedImage[chunks];
		for (int i = 0; i < chunks; i++) {
			try {
				buffImages[i] = ImageIO.read(imgFiles[i]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		type = buffImages[0].getType();
		chunkWidth = buffImages[0].getWidth();
		chunkHeight = buffImages[0].getHeight();

		// Initializing the final image
		BufferedImage finalImg = new BufferedImage(chunkWidth * cols, chunkHeight * rows, type);

		int num = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j, chunkHeight * i, null);
				num++;
			}
		}
		try {
			ImageIO.write(finalImg, "png", new File(lfiles.get(0).getParent() + "\\merged.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}