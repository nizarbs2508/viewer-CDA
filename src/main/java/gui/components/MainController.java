package gui.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.ans.cda.Constant;
import com.ans.cda.I18N;

import gui.TabSpace;
import gui.mediator.Events;
import gui.mediator.IMediator;
import gui.mediator.Mediator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import lib.EditorUtils;
import smallUndoEngine.EditorTextHistory;

/**
 * MainController for xml editor
 * 
 * @author bensa Nizar
 */
public class MainController {

	@FXML
	private TabPane tabPane;

	@FXML
	private TextSpace textSpace;

	@FXML
	private MainMenuBar mainMenuBar;

	@FXML
	private FindReplaceToolBar findReplaceToolBar;

	private IMediator mediator = Mediator.getInstance();

	private ArrayList<TabSpace> tabSpaces = new ArrayList<>();

	private int textSpacesCount = 0;

	/**
	 * initialize
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	@FXML
	public void initialize() throws SAXException, ParserConfigurationException, IOException {
		createNewTab(false);
		mediator.setMainController(this);
		mediator.setTabSpaces(tabSpaces);
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
	}

	/**
	 * tabPaneListener
	 */
	private void tabPaneListener() {
		tabPane.getSelectionModel().selectedIndexProperty().addListener((ov, oldValue, newValue) -> {
			if (tabPane.getTabs().size() == 0) {
				return;
			}
			mediator.getEventBuilder().withEvent(Events.TAB_CHANGED).build();
		});
	}

	/**
	 * @param isSaved: true if a new tab is created as a result of OPEN_MENU event,
	 *                 false otherwise
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @see Mediator creates new tab and a new editorTextHistory if the array of
	 *      tabs is not full
	 */
	public void createNewTab(final boolean isSaved) {
		final Tab tab = new Tab(Constant.untitled_tab + textSpacesCount);
		final TextSpace textSpace = new TextSpace();

		final FindReplaceToolBar findReplaceToolBar = new FindReplaceToolBar();
		findReplaceToolBar.setManaged(false);

		final EditorTextHistory editorTextHistory = new EditorTextHistory();
		textSpace.setNumber(textSpacesCount);

		// VBox used for tab content that hold textspace and toolbar because we can't
		// add many nodes to Tab
		final VBox vBox = new VBox();
		vBox.getChildren().addAll(textSpace, findReplaceToolBar);
		tab.setContent(vBox);

		final TabSpace tabSpace = addTabSpace(textSpace, editorTextHistory, findReplaceToolBar, isSaved);
		tab.setOnCloseRequest(event -> {
			final Alert alert = EditorUtils.createConfirmationAlert(I18N.getString("button.closetxt.text"),
					I18N.getString("button.yes.text"), "");
			boolean close = true;
			if (tabSpace.isTextChanged()) {
				Optional<ButtonType> btnClicked = alert.showAndWait();
				if (!btnClicked.get().getText().equals(I18N.getString("button.yes.text"))) {
					close = false;
					event.consume();
					
				}
			}
			if (close) {
				tabSpaces.remove(getCurrentTabIndex());
			}
		});
		tabPane.getTabs().add(tab);
		textSpacesCount++;
		tabPaneListener();
	}

	/**
	 * adds a new tabspace the the list of tabspaces
	 *
	 * @param textSpace         the current TextSpace of TabSpace
	 * @param editorTextHistory the current EditorTextHistory of TabSpace
	 * @param isSaved           specifies if the file is saved in the system
	 * @return the created tabSpace
	 */
	private TabSpace addTabSpace(final TextSpace textSpace, final EditorTextHistory editorTextHistory,
			final FindReplaceToolBar findReplaceToolBar, final boolean isSaved) {
		final TabSpace current = new TabSpace(textSpace, editorTextHistory, findReplaceToolBar);
		current.setIsSaved(isSaved);
		tabSpaces.add(current);
		return current;
	}

	/**
	 * @return the current selected tab
	 */
	public Tab getCurrentTab() {
		return tabPane.getSelectionModel().getSelectedItem();
	}

	/**
	 * @return tabPane
	 */
	public TabPane getTabPane() {
		return tabPane;
	}

	public void updateIsSaved(final boolean isSaved) {
		tabSpaces.get(getCurrentTabIndex()).setIsSaved(isSaved);
	}

	/**
	 * @return the index of the selected tab
	 */
	public int getCurrentTabIndex() {
		return tabPane.getSelectionModel().getSelectedIndex();
	}

	/**
	 * @return the mainMenuBar
	 */
	public MainMenuBar getMainMenuBar() {
		return mainMenuBar;
	}

	/**
	 * @param mainMenuBar the mainMenuBar to set
	 */
	public void setMainMenuBar(final MainMenuBar mainMenuBar) {
		this.mainMenuBar = mainMenuBar;
	}
}