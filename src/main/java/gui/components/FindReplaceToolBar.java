package gui.components;

import gui.mediator.Events;
import gui.mediator.Mediator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lib.EditorUtils;

/**
 * Find and Replace Tool Bar class
 * 
 * @author bensa Nizar
 */
public class FindReplaceToolBar extends VBox {

	@FXML
	private TextField findTextField;

	@FXML
	private TextField replaceTextField;

	@FXML
	private Button findReplaceButton;

	@FXML
	private Button nextFindButton;

	@FXML
	private Button previousFindButton;

	@FXML
	private Text findReplaceWordCount;

	@FXML
	private Text findReplaceHighlightedCount;

	@FXML
	private CheckBox replaceAllCheckbox;

	@FXML
	private CheckBox caseSensetiveCheckBox;

	@FXML
	private HBox findHbox;

	@FXML
	private HBox replaceHbox;

	@FXML
	private Button hideFindReplaceToolBarButton;

	@FXML
	private ToolBar findToolBar;

	@FXML
	private ToolBar replaceToolBar;

	private Mediator mediator = Mediator.getInstance();

	private int currentSelectedMatch = 0;

	private int matchedCount;

	/**
	 * FindReplaceToolBar
	 */
	public FindReplaceToolBar() {
		final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/findAndReplace.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		try {
			fxmlLoader.load();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * initialize
	 */
	@FXML
	public void initialize() {
		mediator.setFindReplaceToolBar(this);
		findTextField.textProperty()
				.addListener((observable, oldValue, newValue) -> findReplaceTextFieldChangeListener());
		caseSensetiveCheckBox.selectedProperty()
				.addListener((observable, oldValue, newValue) -> findReplaceTextFieldChangeListener());
	}

	/**
	 * replaceButtonPressed
	 * 
	 * @param event
	 */
	@FXML
	public void replaceButtonPressed(final ActionEvent event) {
		if (replaceAllCheckbox.isSelected()) {
			mediator.getEventBuilder().withText(replaceTextField.getText()).withEvent(Events.REPLACE_ALL).build();
		} else {
			mediator.getEventBuilder().withText(replaceTextField.getText()).withEvent(Events.REPLACE_CURRENT).build();
		}
	}

	/**
	 * hideFindReplaceToolBarButtonPressed
	 * 
	 * @param event
	 */
	@FXML
	public void hideFindReplaceToolBarButtonPressed(final ActionEvent event) {
		mediator.getEventBuilder().withEvent(Events.HIDE_REPLACE).build();
	}

	/**
	 * nextFindButtonPressed
	 */
	@FXML
	public void nextFindButtonPressed() {
		if (currentSelectedMatch + 1 <= matchedCount) {
			currentSelectedMatch++;
		}
		if (matchedCount > 0) {
			findReplaceHighlightedCount.setText(currentSelectedMatch + " of ");
		}
		mediator.getEventBuilder().withEvent(Events.FIND_NEXT).build();
	}

	/**
	 * previousFindButtonPressed
	 */
	@FXML
	public void previousFindButtonPressed() {
		if (currentSelectedMatch - 1 > 0) {
			currentSelectedMatch--;
		}
		if (matchedCount > 0) {
			findReplaceHighlightedCount.setText(currentSelectedMatch + " of ");
		}
		mediator.getEventBuilder().withEvent(Events.FIND_PREVIOUS).build();
	}

	/**
	 * findReplaceTextFieldChangeListener
	 */
	private void findReplaceTextFieldChangeListener() {
		currentSelectedMatch = 0;
		findReplaceHighlightedCount.setText("");
		final String text = mediator.getText();
		final String substring = findTextField.getText();
		matchedCount = EditorUtils.getSubstringMatchedCount(substring, text, caseSensetiveCheckBox.isSelected());
		findReplaceWordCount.setText(matchedCount + "\nmatches");

		if (matchedCount > 0) {
			currentSelectedMatch++;
			findReplaceHighlightedCount.setText(currentSelectedMatch + " of ");
		}

		mediator.getEventBuilder().withEvent(Events.FIND_SELECT).withText(substring).build();
	}

	/**
	 * setReplaceToolbarVisibility
	 * 
	 * @param visibility
	 */
	private void setReplaceToolbarVisibility(final boolean visibility) {
		replaceToolBar.setVisible(visibility);
		replaceToolBar.setManaged(visibility);
	}

	/**
	 * Shows replaceToolBar
	 */
	public void showFindReplace() {
		setReplaceToolbarVisibility(true);
		this.setVisible(true);
		this.setManaged(true);
	}

	/**
	 * Hides FindAndReplaceToolbar by hiding Vbox
	 */
	public void hideFindReplace() {
		this.setVisible(false);
		this.setManaged(false);
	}

	/**
	 * Shows findToolBar
	 */
	public void showFindToolbar() {
		setReplaceToolbarVisibility(false);
		this.setVisible(true);
		this.setManaged(true);
	}

	/**
	 * isMatchCase
	 * 
	 * @return
	 */
	public boolean isMatchCase() {
		return caseSensetiveCheckBox.isSelected();
	}
}