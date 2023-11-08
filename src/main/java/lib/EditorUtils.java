package lib;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ans.cda.Constant;
import com.ans.cda.I18N;

import gui.components.MainMenuBar;
import gui.mediator.Events;
import gui.mediator.Mediator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Editor Utils class : class utilitaire
 * 
 * @author bensa Nizar
 */
public class EditorUtils {

	public static Stage stageprimary;

	/**
	 * writeToFile
	 * 
	 * @param text
	 * @param path
	 * @return
	 */
	public static boolean writeToFile(final String text, final Path path) {
		if (path == null || text == null) {
			return false;
		}
		try {
			Files.write(path.toAbsolutePath(), text.getBytes("utf-8"), StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * shows the version information of the app
	 */
	public static void showAboutWindow(MainMenuBar mainMenuBar) {
		final FXMLLoader fxmlLoader = new FXMLLoader(mainMenuBar.getClass().getResource("/about.fxml"));
		Parent root = null;
		try {
			root = fxmlLoader.load();
			Scene scene = mainMenuBar.getScene();
			scene.getStylesheets().add(mainMenuBar.getClass().getResource("/style.css").toExternalForm());
			final Stage stage = new Stage();
			stage.setResizable(false);
			stage.setTitle(I18N.getString("button.propos.text"));
			stage.setScene(new Scene(root, 290, 130));
			stage.showAndWait();
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * onCloseExitConfirmation
	 * 
	 * @param stage
	 */
	public static void onCloseExitConfirmation(final Stage stage) {
		if (Mediator.getInstance().shouldExit()) {
			final Alert alert = createConfirmationAlert(I18N.getString("button.closeevent.text"),
					I18N.getString("button.yes.text"), I18N.getString("button.no.text"));
			final DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().add(EditorUtils.class.getResource("/style.css").toExternalForm());
			dialogPane.getStyleClass().add("myDialog");
			dialogPane.setMinHeight(150);
			dialogPane.setMaxHeight(150);
			dialogPane.setPrefHeight(150);
			final Optional<ButtonType> btnClicked = alert.showAndWait();
			if (btnClicked.get().getText().equals(I18N.getString("button.yes.text"))) {
				if (Mediator.getInstance().isFileSaved()) {
					Mediator.getInstance().getEventBuilder().withEvent(Events.AUTO_SAVE).build();
					stage.close();
				} else {
					Mediator.getInstance().getEventBuilder().withEvent(Events.SAVE_REQUEST).build();
					stage.close();
				}
			} else if (btnClicked.get().getText().equals(I18N.getString("button.no.text"))) {
				stage.close();
			}
		} else {
			stage.close();
		}
	}

	/**
	 * creates an alert confirmation window with 2/3 buttons, 2 custom buttons, 1
	 * cancel button
	 *
	 * @param alertText:   the content of the alert
	 * @param buttonText1: the text value of the first button
	 * @param buttonText2: the text value of the second button
	 *                     <p>
	 *                     usage: Alert alert =
	 *                     EditorUtils.createConfirmationAlert("Do you want to save
	 *                     your changes before quitting?", "Yes", "No");
	 *                     Optional<ButtonType> btnClicked = alert.showAndWait();
	 *                     if(btnClicked.get().getText().equals("type"){ // do
	 *                     something } ...
	 *                     <p>
	 *                     you can create one custom button only, just pass "" as an
	 *                     argument for buttonText2
	 *                     <p>
	 *
	 */
	public static Alert createConfirmationAlert(final String alertText, final String buttonText1,
			final String buttonText2) {
		final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(I18N.getString("button.quit.text"));
		alert.setHeaderText(null);
		alert.setContentText(alertText);
		final ButtonType btn1 = new ButtonType(buttonText1);
		final ButtonType btn2 = new ButtonType(buttonText2);
		final ButtonType cnlBtn = new ButtonType(I18N.getString("button.cancel.text"),
				ButtonBar.ButtonData.CANCEL_CLOSE);
		if (buttonText2.isEmpty()) {
			alert.getButtonTypes().setAll(btn1, cnlBtn);
			return alert;
		}
		alert.getButtonTypes().setAll(btn1, btn2, cnlBtn);
		return alert;
	}

	/**
	 * updates the stage title whenever the selected tab changes
	 *
	 * @param tabPane:   the TabPane of the main controller
	 * @param filePath:  the path of the opened file in the selected tab
	 * @param tabNumber: the index of the selected tab
	 *                   <p>
	 *                   Getting the tab using the index of the selected tab is used
	 *                   because tabPane...getSelectedTab somehow returns the
	 *                   previous selected tab,
	 */
	public static void setCurrentEditorTitle(final TabPane tabPane, final Path filePath, final int tabNumber) {
		final Stage stage = (Stage) tabPane.getParent().getScene().getWindow();

		if (filePath == null) {
			stage.setTitle(Constant.untitled_tab + tabNumber);
			return;
		}

		stage.setTitle(filePath.toString());
		tabPane.getTabs().get(tabNumber).setText(filePath.getFileName().toString());
	}

	/**
	 * updates the stage title used whenever the user opens a new file, or save a
	 * new file
	 *
	 * @param filePath: the path of the saved file
	 * @param tabPane:  the current tabPane, used to get the stage
	 */
	public static void setStageTitle(final TabPane tabPane, final Path filePath) {
		final Stage stage = (Stage) tabPane.getParent().getScene().getWindow();
		stage.setTitle(filePath.toString());
		stageprimary = stage;
	}

	/**
	 * getStageTitle
	 * 
	 * @return
	 */
	public static Stage getStageTitle() {
		return getStageprimary();
	}

	/**
	 * updates the stage title used whenever the user opens a new file, or save a
	 * new file
	 *
	 * @param filePath:  the path of the saved file
	 * @param tabPane:   the current tabPane, used to get the stage, and the
	 *                   selected tab
	 * @param tabNumber: the index of the selected tab
	 */
	public static void setTabTitle(final TabPane tabPane, final Path filePath, final int tabNumber) {
		tabPane.getTabs().get(tabNumber).setText(filePath.getFileName().toString());
	}

	public static List<String> readFromFile(final File file) {
		List<String> lines = new ArrayList<>();
		try {
			lines = Files.readAllLines(Paths.get(file.getPath()), StandardCharsets.UTF_8);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	/**
	 * opens a fileChooser save windows so the user can save a new file as .txt does
	 * nothing if the file in null creates a new text file with the current text in
	 * the specified path sends SAVE_MENU event to the mediator
	 *
	 * @see Mediator
	 * @see EditorUtils#writeToFile(String, Path)
	 */
	public static Path showSaveWindow(final Window window) {
		final Mediator mediator = Mediator.getInstance();
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(I18N.getString("button.save.text"));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
		File file = fileChooser.showSaveDialog(window);
		if (file == null) { // check if the user clicked the cancel button
			return null;
		}
		file = new File(file.getPath() + ".xml"); // might be only in linux that the file is not saved as title.txt
		EditorUtils.writeToFile(mediator.getText(), file.toPath());
		final Path filePath = file.toPath();
		return filePath;
	}

	/**
	 * @param str:  matcher string
	 * @param text: matched string
	 * @return the number of matcher substrings in the matched string
	 */
	public static int getSubstringMatchedCount(String str, String text, final boolean matchCase) {

		if (!matchCase) {
			text = text.toLowerCase();
			str = str.toLowerCase();
		}
		if (str.isEmpty()) {
			return 0;
		}
		final Pattern pattern = Pattern.compile(Pattern.quote(str));
		final Matcher matcher = pattern.matcher(text);
		int count = 0;
		while (matcher.find()) {
			count++;
		}
		return count;
	}

	/**
	 * @return List of Integers of each index where the substring start in text
	 */
	public static List<Integer> getIndexStartsOfSubstring(String text, String substring, final boolean matchCase) {
		List<Integer> startIndices = new ArrayList<>();
		if (!matchCase) {
			text = text.toLowerCase();
			substring = substring.toLowerCase();
		}

		int indexStart = 0;
		int count = EditorUtils.getSubstringMatchedCount(substring, text, matchCase);

		for (int i = 0; i < count; i++) {
			indexStart = text.indexOf(substring, indexStart);
			startIndices.add(indexStart);
			indexStart += substring.length();
		}

		return startIndices;
	}

	/**
	 * @param text:    text
	 * @param current: the replaced string
	 * @param str:     the new string
	 * @param index:   the ith occurence of current in text
	 * @return text with current string replaced with str
	 *
	 */
	public static String replaceSpecificString(String text, String current, String str, int index, boolean matchCase)
			throws Exception {

		if (!matchCase) {
			text = text.toLowerCase();
			current = current.toLowerCase();
		}

		final List<Integer> startIndices = getIndexStartsOfSubstring(text, current, matchCase);

		if (index >= startIndices.size()) {
			throw new Exception("text doesn't contain " + index + "th " + str);
		}

		final int startIndex = startIndices.get(index);
		final String newText = new StringBuilder(text).replace(startIndex, startIndex + current.length(), str)
				.toString();

		return newText;

	}

	/**
	 * @return the stageprimary
	 */
	public static Stage getStageprimary() {
		return stageprimary;
	}

	/**
	 * @param stageprimary the stageprimary to set
	 */
	public static void setStageprimary(final Stage stageprimary) {
		EditorUtils.stageprimary = stageprimary;
	}
}