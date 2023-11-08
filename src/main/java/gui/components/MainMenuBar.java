package gui.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import com.ans.cda.Constant;
import com.ans.cda.Handler;
import com.ans.cda.I18N;
import com.ans.cda.WebViewSample;
import com.aspose.barcode.generation.BarCodeImageFormat;
import com.aspose.barcode.generation.BarcodeGenerator;
import com.aspose.barcode.generation.DataMatrixEncodeMode;
import com.aspose.barcode.generation.EncodeTypes;

import gui.mediator.Events;
import gui.mediator.IMediator;
import gui.mediator.Mediator;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.skin.TextAreaSkin;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lib.EditorUtils;
import smallUndoEngine.EditorTextHistory;

/**
 * Main Menu Bar class
 * 
 * @author bensa Nizar
 */
public class MainMenuBar extends MenuBar {

	@FXML
	private MenuItem undo;

	@FXML
	private MenuItem save;

	@FXML
	private MenuItem redo;

	@FXML
	private MenuItem close;

	@FXML
	private MenuItem newTab;

	@FXML
	private MenuItem open;

	@FXML
	private MenuItem copy;

	@FXML
	private MenuItem cut;

	@FXML
	private MenuItem paste;

	@FXML
	private MenuItem findAndReplace;

	@FXML
	private MenuItem find;

	@FXML
	private MenuItem about;

	@FXML
	private MenuItem tree;

	@FXML
	private MenuItem view;

	private IMediator mediator = Mediator.getInstance();

	private String text;

	private Path filePath;

	private Stage secondStage;

	private TextArea textAr = new TextArea();

	private String xslLine;

	private String xslLineAutoP;

	private WebView browser = new WebView();

	private WebEngine webEngine = browser.getEngine();

	private static Stage stage = new Stage();

	private Integer count = 1;

	/**
	 * constructor
	 */
	public MainMenuBar() {
		final Locale local = I18N.getLocale();
		final ResourceBundle bundle = ResourceBundle.getBundle("messages", local);
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menubar.fxml"), bundle);
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
			// keybord controls
			save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
			copy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN));
			paste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
			cut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
			find.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
			undo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
			redo.setAccelerator(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN));
			findAndReplace.setAccelerator(new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN));
			close.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
			about.setAccelerator(new KeyCodeCombination(KeyCode.F1));
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
	void viewMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.VIEW).build();
		final String xmlStr = mediator.getMediatorFilePath().toString();
		WebViewSample webViewSample = new WebViewSample();
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
		updatePane.setStyle("-fx-background-color: #edf2f4");

		final Stage taskUpdateStage = new Stage(StageStyle.UNDECORATED);
		taskUpdateStage.setScene(new Scene(updatePane, 170, 170));
		// End progressBar
		try {
			readFileContents(new File(xmlStr));
			if (xslLine != null && !xslLine.isEmpty()) {
				// we would like to get the substring between '[' and ']'
				final int start = xslLine.indexOf("href=\"") + "href=\"".length();
				final int end = xslLine.indexOf("\"?>");
				final String outStr = xslLine.substring(start, end);
				String finalStr = null;
				File xslFile = null;
				final String xmlFileParent = new File(xmlStr).getParentFile().getParentFile().getAbsolutePath();
				if (outStr.contains("..") && xslLineAutoP == null) {
					finalStr = outStr.substring(2);
					xslFile = new File(xmlFileParent + finalStr);
				} else if (!outStr.contains("..") && xslLineAutoP == null) {
					finalStr = outStr;
					xslFile = new File(xmlFileParent + finalStr);
				} else if (xslLineAutoP != null) {
					xslFile = new File(xmlStr);
				}
				if (xslFile.exists()) {
					final String xslPath = xslFile.getAbsolutePath();
					webViewSample.runTask(taskUpdateStage, progress);
					Platform.runLater(() -> {
						Popup popup = new Popup();
						// create temp html file
						Path file1;
						try {
							file1 = Files.createTempFile(null, Constant.htmlext);
							final String streamRes = webViewSample.transform(xslPath, xmlStr);
							// Writes a string to the above temporary file
							Files.write(file1, streamRes.getBytes(StandardCharsets.UTF_8));
							final File url1 = new File(file1.toString());
							webEngine.load((url1.toURI()).toString());
							// retrieving the observable list of the VBox
							final ObservableList<Node> list = popup.getContent();
							// Adding all the nodes to the observable list
							list.addAll(browser);

							// Start DATAMATRIX
							final InputStream targetStream = new FileInputStream(url1);
							final Document doc = Jsoup.parse(targetStream, Constant.utf8, "");
							// Get Element datamatrix for make some changes
							final Element content = doc.getElementById(Constant.element);
							if (content != null) {
								final String dataMatrixValue = content.val();

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

								final Element img = new Element(Tag.valueOf("img"), "").attr("src",
										"file:///" + matrix.toString());
								content.appendChild(img);
								content.val("");
								content.attr("style", "padding:0px 35px;");
							}
							// e-prescription Matrix
							// Get Element datamatrix for make some changes
							final Element contentEprescription = doc.getElementById(Constant.elementMatrix);
							if (contentEprescription != null) {
								final String dataMatrixValueEprescription = contentEprescription.val();
								// Initialiser un objet de la classe BarcodeGenerator
								final BarcodeGenerator genEprescription = new BarcodeGenerator(EncodeTypes.DATA_MATRIX,
										dataMatrixValueEprescription);
								// D�finir les pixels
								genEprescription.getParameters().getBarcode().getXDimension().setPixels(4);
								// R�glez le mode d'encodage sur Auto
								genEprescription.getParameters().getBarcode().getDataMatrix()
										.setDataMatrixEncodeMode(DataMatrixEncodeMode.C40);

								final Path matrixEprescription = Files.createTempFile(null, Constant.pngext);
								genEprescription.save(matrixEprescription.toString(), BarCodeImageFormat.PNG);
								final Element imgEprescription = new Element(Tag.valueOf("img"), "").attr("src",
										"file:///" + matrixEprescription.toString());
								contentEprescription.appendChild(imgEprescription);

								contentEprescription.val("");
								contentEprescription.attr("style", "padding:0px 35px;");
							}
							// Start PDF
							final Element iframe = doc.getElementById(Constant.frame);
							if (iframe != null) {
								final Element iframeChild = iframe.firstElementChild();
								final String source = iframeChild.attr("src");
								final String find = Constant.b64;
								final boolean isFound = source.contains(Constant.b64);
								if (isFound) {
									final int i = source.indexOf(find);
									if (i > 0) {
										final String s1 = source.substring(i + 7);
										final String b64 = s1.trim();
										final Path pdf = Files.createTempFile(null, Constant.pdfext);
										final File file = new File(pdf.toString());
										try (final FileOutputStream fos = new FileOutputStream(file);) {
											final byte[] decoder = Base64.getDecoder().decode(b64);
											fos.write(decoder);
										} catch (final Exception e) {
											e.printStackTrace();
										}
										// PDF to Images
										final List<File> lfiles = webViewSample.generateImageFromPDF(file);
										if (!lfiles.isEmpty() && lfiles.size() != 0) {
											for (int j = 0; j < lfiles.size(); j++) {
												final String name = lfiles.get(j).getPath();
												final Element image = new Element(Tag.valueOf("img"), "").attr("src",
														"file:///" + name);
												final Element br = new Element(Tag.valueOf("br"), "");
												if (j == 0) {
													iframeChild.replaceWith(image);
													iframe.appendChild(br);
												} else {
													iframe.appendChild(image);
													iframe.appendChild(br);
												}
											}
										}
										// END PDF to Images
									}
								}
							}
							// FIN DATAMATRIX
							final String docHtml = doc.html();
							final PrintWriter printWriter = new PrintWriter(url1, Constant.utf8);
							printWriter.print("");
							printWriter.print(docHtml);
							printWriter.close();
							webEngine.load(new File(url1.getPath()).toURI().toURL().toString());
							browser.prefHeightProperty().bind(stage.heightProperty());
							browser.prefWidthProperty().bind(stage.widthProperty());
							VBox layout = new VBox();
							final Scene scene = new Scene(layout);
							layout.getChildren().addAll(browser);
							stage.setScene(scene);
							stage.setMaximized(true);
							stage.setMaxWidth(Integer.MAX_VALUE);
							stage.setMaxHeight(Integer.MAX_VALUE);
							stage.setTitle(xmlStr);
							stage.getIcons()
									.add(new Image(getClass().getClassLoader().getResourceAsStream(Constant.photo)));
							stage.setOnCloseRequest(eventt -> {
								eventt.consume();
								if (stage != null) {
									popup.hide();
									stage.close();
									xslLine = null;
									xslLineAutoP = null;
								}
							});
							stage.showAndWait();
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
						} catch (IOException e) {
							e.printStackTrace();
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
					alert.setContentText(I18N.getString("popup.error8"));
					alert.setHeaderText(null);
					alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(true);
					alert.showAndWait();
				}

			} else {
				final Alert alert = new Alert(AlertType.WARNING);
				final DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
				dialogPane.getStyleClass().add("myDialog");
				dialogPane.setMinHeight(130);
				dialogPane.setMaxHeight(130);
				dialogPane.setPrefHeight(130);
				alert.setContentText(I18N.getString("popup.error8"));
				alert.setHeaderText(null);
				alert.getDialogPane().lookupButton(ButtonType.OK).setVisible(true);
				alert.showAndWait();
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param event javafx event.. sends an event to the mediator when a new tab is
	 *              created
	 * @see Mediator
	 **/
	@FXML
	void onNewTabClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.NEW_TAB).build();
	}

	/**
	 * @param event javafx event.. sends AUTO_SAVE event to mediator if the file is
	 *              saved to save the file without opening the fileChooser save
	 *              windows otherwise shows the fileChooser save windows and sends a
	 *              SAVE_MENU to save the file
	 * @see Mediator
	 */
	@FXML
	void saveMenuItemClick(final ActionEvent event) {
		if (mediator.isFileSaved()) {
			mediator.getEventBuilder().withEvent(Events.AUTO_SAVE).build();
		} else {
			filePath = EditorUtils.showSaveWindow(save.getParentPopup().getScene().getWindow());
			if (filePath == null) {
				return;
			}
			mediator.getEventBuilder().withEvent(Events.SAVE_MENU).fileSaved(true).textChanged(false)
					.withFilePath(filePath).build();

		}
	}

	/**
	 * readFileContents
	 * 
	 * @param selectedFile
	 * @throws IOException
	 */
	private String readFileContents(final File file) throws IOException {
		final BufferedReader br = new BufferedReader(new FileReader(file));
		String singleString = null;

		try {
			final StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(count++);
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
				if (line != null && line.startsWith("<?xml-stylesheet")) {
					xslLine = line;
				}
				if (line != null && line.startsWith("<xsl:stylesheet")) {
					xslLineAutoP = line;
				}
			}
			singleString = sb.toString();
		} finally {
			br.close();
			count = 1;
		}
		return singleString;
	}

	/**
	 * @param event javafx event.. sends a CLOSE_MENU event to the mediator pops the alert confirmation window
	 * @see EditorUtils
	 * @see Mediator
	 */
	@FXML
	void treeMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.TREE_FILE).build();
		Platform.setImplicitExit(false);
		secondStage = new Stage();
		secondStage.setOnShowing(ev -> {
			final String xmlStr = mediator.getMediatorFilePath().toString();
			final File file = new File(xmlStr);
			if (file != null) {
				try {
					final TreeItem<String> root = Handler.readXML(new File(xmlStr));
					final TreeView<String> treeView = new TreeView<String>(root);
					MultipleSelectionModel<TreeItem<String>> tvSelModel = treeView.getSelectionModel();
					tvSelModel.selectedItemProperty().addListener(new ChangeListener<TreeItem<String>>() {
						public void changed(ObservableValue<? extends TreeItem<String>> changed,
								TreeItem<String> oldVal, TreeItem<String> newVal) {
							// Display the selection and its complete path from the root.
							int total = 0;
							final TreeItem<String> selectedItem = (TreeItem<String>) newVal;
							try {
								textAr.setText(readFileContents(file));
							} catch (final IOException e) {
								e.printStackTrace();
							}
							if (selectedItem.getValue().contains("[")) {
								final String str = selectedItem.getValue()
										.substring(selectedItem.getValue().lastIndexOf("[") + 1);
								final String[] words = str.split("]");

								String strFinal = null;
								for (final String word : words) {
									strFinal = word;
								}

								// Define desired line
								final int line = Integer.valueOf(strFinal);
								final String[] lines = textAr.getText().split("\n");
								for (Integer i = 0; i < line; i++) {
									final int numChars = lines[i].length();
									total += numChars;
								}

								final Rectangle2D lineBounds = ((TextAreaSkin) textAr.getSkin())
										.getCharacterBounds(total);
								textAr.setScrollTop(lineBounds.getMinY());

							} else {
								if (selectedItem.getParent().getValue().contains("[")) {
									final String str = selectedItem.getParent().getValue()
											.substring(selectedItem.getParent().getValue().lastIndexOf("[") + 1);
									final String[] words = str.split("]");

									String strFinal = null;
									for (final String word : words) {
										strFinal = word;
									}
									// Define desired line
									final int line = Integer.valueOf(strFinal);
									final String[] lines = textAr.getText().split("\n");
									for (Integer i = 0; i < line; i++) {
										int numChars = lines[i].length();
										total += numChars;

									}

									final Rectangle2D lineBounds = ((TextAreaSkin) textAr.getSkin())
											.getCharacterBounds(total);
									textAr.setScrollTop(lineBounds.getMinY());
								}
							}
						}
					});

					final StackPane stack = new StackPane();
					textAr.setEditable(true);
					textAr.setPrefSize(1000, 1000);
					textAr.setText(readFileContents(file));

					stack.getStyleClass().add("stack-pane");
					stack.setPadding(new Insets(5));
					stack.getChildren().addAll(treeView);
					final HBox listHeaderBox2 = new HBox();
					listHeaderBox2.setAlignment(Pos.BASELINE_RIGHT);
					listHeaderBox2.getChildren().add(textAr);
					final StackPane sp5 = new StackPane();
					sp5.getStyleClass().add("stack-pane");
					sp5.getChildren().add(listHeaderBox2);
					final SplitPane sp3 = new SplitPane();
					sp3.getStyleClass().add("stack-pane");
					sp3.getItems().addAll(sp5);
					final StackPane sp2 = new StackPane();
					sp2.getStyleClass().add("stack-pane");
					sp2.getChildren().add(stack);
					final SplitPane sp = new SplitPane();
					sp.getStyleClass().add("stack-pane");
					sp.getItems().addAll(sp2, sp3);
					final Scene scene = new Scene(sp);
					scene.getStylesheets().add(MainMenuBar.class.getResource("/style.css").toExternalForm());
					Platform.runLater(() -> {
						secondStage.setScene(scene);
						secondStage.setTitle(file.getAbsolutePath());
						secondStage.setMaxWidth(Integer.MAX_VALUE);
						secondStage.setMaxHeight(Integer.MAX_VALUE);
						secondStage.setMaximized(true);
						secondStage.getIcons()
								.add(new Image(getClass().getClassLoader().getResourceAsStream(Constant.photo)));
					});

					scene.widthProperty().addListener(new ChangeListener<Number>() {
						@Override
						public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth,
								Number newSceneWidth) {
							if (newSceneWidth.doubleValue() >= 1509 && newSceneWidth.doubleValue() < 1632) { // ecran 16
																												// pouces
								textAr.setStyle(
										"-fx-font-size:14pt; -fx-font-weight:normal; -fx-font-family:Monaco, 'Courier New', MONOSPACE; -fx-background-color: #e9e4e6;");
							}
							if (newSceneWidth.doubleValue() >= 1632 && newSceneWidth.doubleValue() >= 1728) { // ecran
																												// 17
																												// pouces
								textAr.setStyle(
										"-fx-font-size:16pt; -fx-font-weight:normal; -fx-font-family:Monaco, 'Courier New', MONOSPACE; -fx-background-color: #e9e4e6;");
							} else if (newSceneWidth.doubleValue() < 1509) {
								textAr.setStyle(
										"-fx-font-size:10pt; -fx-font-weight:normal; -fx-font-family:Monaco, 'Courier New', MONOSPACE; -fx-background-color: #e9e4e6;");
							}
						}
					});

				} catch (final Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		secondStage.setOnCloseRequest(ev -> {
			ev.consume();
			secondStage.close();
		});
		secondStage.show();
	}

	/**
	 * closeMenuItemClick
	 * 
	 * @param event
	 */
	@FXML
	void closeMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.CLOSE_MENU).build();
		Stage stage = mediator.getStageTitle();
		EditorUtils.onCloseExitConfirmation(stage);
	}

	/**
	 * @param event javafx event.. sends a UNDO_TEXT event to the mediator to undo
	 *              the current text
	 * @see Mediator
	 * @see EditorTextHistory
	 */
	@FXML
	void undoMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.UNDO_TEXT).build();
	}

	/**
	 * @param event javafx event.. sends a REDO_TEXT event to the mediator to undo
	 *              the current text
	 * @see Mediator
	 * @see EditorTextHistory
	 */
	@FXML
	void redoMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.REDO_TEXT).build();
	}

	/**
	 * @param event javafx event.. sends a COPY_MENU event to mediator to copy the
	 *              selected text to clipboards
	 * @see Mediator
	 */
	@FXML
	void copyMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.COPY_MENU).build();
		final Map<DataFormat, Object> hashMap = new HashMap<>();
		hashMap.put(DataFormat.PLAIN_TEXT, mediator.getMediatorText());
		Clipboard.getSystemClipboard().setContent(hashMap);
	}

	/**
	 * @param event javafx event.. sends a CUT_MENU event to mediator and copies the
	 *              selected text to clipboards then deletes it from textArea
	 * @see Mediator
	 */
	@FXML
	void cutMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.CUT_MENU).build();
		final Map<DataFormat, Object> hashMap = new HashMap<>();
		hashMap.put(DataFormat.PLAIN_TEXT, mediator.getMediatorText());
		Clipboard.getSystemClipboard().setContent(hashMap);
	}

	/**
	 * @param event javafx event.. sends a PASTE_MENU event to mediator and adds the
	 *              text in clipboards to textArea starting from the position of the
	 *              carret
	 * @see Mediator
	 */
	@FXML
	void pasteMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.PASTE_MENU).build();
	}

	/**
	 * findAndReplaceMenuItemClick
	 * 
	 * @param event
	 */
	@FXML
	void findAndReplaceMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.SHOW_FIND_REPLACE).build();
	}

	/**
	 * findMenuItemClick
	 * 
	 * @param event
	 */
	@FXML
	void findMenuItemClick(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.SHOW_FIND).build();
	}

	/**
	 * shows the version information of the app
	 *
	 * @see EditorUtils#showAboutWindow(MainMenuBar)
	 */
	@FXML
	void aboutMenuItemClick(ActionEvent event) {
		EditorUtils.showAboutWindow(this);
		mediator.getEventBuilder().withEvent(Events.ABOUT_MENU).build();
	}

	/**
	 * sets the current menubar in mediator to this instance
	 *
	 * @see Mediator
	 */
	@FXML
	public void initialize() {
		mediator.setMenuBar(this);
	}

	/**
	 * @return the text in textArea
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param file the text file to read from a task to read the text file in
	 *             another thread waits for the thread to finish, then sends
	 *             OPEN_MENU event to the mediator to update the textArea text if
	 *             the file reading fails, it sets the current text to "FAILED"
	 * @see Mediator
	 * @see EditorUtils
	 */
	public void readFile(final File file, final String xslFile) {
		final List<String> list = EditorUtils.readFromFile(file);
		final String str = list.stream().collect(Collectors.joining("\n"));
		setCurrentText(str);
		mediator.getEventBuilder().withEvent(Events.OPEN_MENU).withFilePath(file.toPath()).withText(text)
				.textChanged(false).fileSaved(true).build();
		mediator.setXslFilePath(xslFile);
		mediator.getStageTitle().getIcons()
				.add(new Image(getClass().getClassLoader().getResourceAsStream(Constant.photo)));
	}

	/**
	 * @param text the text that will be displayed in textArea used to update
	 *             textArea's text when reading a file
	 **/
	private void setCurrentText(final String text) {
		this.text = text;
	}

	/**
	 * @return the secondStage
	 */
	public Stage getSecondStage() {
		return secondStage;
	}

	/**
	 * @return the stage
	 */
	public static Stage getStage() {
		return stage;
	}

}