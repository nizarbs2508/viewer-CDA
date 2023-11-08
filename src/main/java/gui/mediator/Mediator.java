package gui.mediator;

import static gui.mediator.Events.COPY_MENU;
import static gui.mediator.Events.CUT_MENU;
import static gui.mediator.Events.FIND_NEXT;
import static gui.mediator.Events.FIND_PREVIOUS;
import static gui.mediator.Events.FIND_SELECT;
import static gui.mediator.Events.HIDE_REPLACE;
import static gui.mediator.Events.OPEN_MENU;
import static gui.mediator.Events.PASTE_MENU;
import static gui.mediator.Events.REDO_TEXT;
import static gui.mediator.Events.REPLACE_ALL;
import static gui.mediator.Events.REPLACE_CURRENT;
import static gui.mediator.Events.SAVE_MENU;
import static gui.mediator.Events.SHOW_FIND;
import static gui.mediator.Events.SHOW_FIND_REPLACE;
import static gui.mediator.Events.TEXT_CHANGED;
import static gui.mediator.Events.UNDO_TEXT;

import java.nio.file.Path;
import java.util.List;

import gui.TabSpace;
import gui.components.FindReplaceToolBar;
import gui.components.MainController;
import gui.components.MainMenuBar;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import lib.EditorUtils;

/**
 * Class Mediator
 * 
 * @author bensa Nizar
 */
public class Mediator implements IMediator {
	private Path filePath;
	private MainController mainController;
	private boolean fileSaved;
	private boolean textChanged;
	private String text;
	public MainMenuBar mainMenuBar;
	private FindReplaceToolBar findReplaceToolBar;
	private List<TabSpace> tabSpaces;
	private String xslFilePath;
	public WebEngine webEngine;

	/**
	 * MediatorInstance class
	 */
	private static final class MediatorInstance {
		private static Mediator INSTANCE = new Mediator();
	}

	/**
	 * getInstance
	 * 
	 * @return instance
	 */
	public static Mediator getInstance() {
		return MediatorInstance.INSTANCE;
	}

	/**
	 * setMenuBar
	 */
	@Override
	public void setMenuBar(final MainMenuBar mainMenuBar) {
		this.mainMenuBar = mainMenuBar;
	}

	/**
	 * setTabSpaces
	 */
	@Override
	public void setTabSpaces(final List<TabSpace> tabSpaces) {
		this.tabSpaces = tabSpaces;
	}

	/**
	 * setMainController
	 */
	@Override
	public void setMainController(final MainController mainController) {
		this.mainController = mainController;
	}

	/**
	 * setFindReplaceToolBar
	 */
	@Override
	public void setFindReplaceToolBar(final FindReplaceToolBar findReplaceToolBar) {
		this.findReplaceToolBar = findReplaceToolBar;
	}

	/**
	 * @param text: the updated text a setter for text
	 */
	public void setText(final String text) {
		this.text = text;
	}

	/**
	 * @return true if the current tab has a file that is saved, false otherwise.
	 */
	public boolean isFileSaved() {
		int tabIndex = mainController.getCurrentTabIndex();
		return tabSpaces.get(tabIndex).isFileSaved();
	}

	/**
	 * checks whether to show the confirmation window when the user exits the app or
	 * not by checking if all tabspaces have no changed text
	 * 
	 * @return AND of tabspaces.isTextChanged() ie true if any value is true in
	 *         tabspaces, false otherwise
	 */

	public boolean shouldExit() {
		return tabSpaces.stream().anyMatch(tabSpace -> tabSpace.isTextChanged());
	}

	/**
	 * @return the text in the selected tab
	 */
	public String getText() {
		final int tabIndex = mainController.getCurrentTabIndex();
		return tabSpaces.get(tabIndex).getText();
	}

	/**
	 * @return the mediator's text, used by textSpace when OPEN_MENU ie gets the
	 *         text from the file, to the mediator, then from the mediator, to
	 *         textspace
	 */
	public String getMediatorText() {
		return text;
	}

	/**
	 * @return the mediator's filepath, used by mediator to update the title when
	 *         OPEN_MENU
	 */
	public Path getMediatorFilePath() {
		return filePath;
	}

	public boolean isMatchCase() {
		return findReplaceToolBar.isMatchCase();
	}

	/**
	 * @return the Path of the file that is opened in the selected tab
	 */
	@Override
	public Path getFilePath() {
		final int tabIndex = mainController.getCurrentTabIndex();
		return tabSpaces.get(tabIndex).getCurrentPath();
	}

	/**
	 * notify event
	 * @param event
	 */
	private void notify(final Events event) {
		int tabIndex = mainController.getCurrentTabIndex();
		switch (event) {
		case TEXT_CHANGED:
			tabSpaces.get(tabIndex).sendEvent(TEXT_CHANGED);
			updateTabTitle(tabIndex);
			break;
		case NEW_TAB:
			mainController.createNewTab(false);
			getMediatorFilePath();
			break;
		case UNDO_TEXT:
			tabSpaces.get(tabIndex).sendEvent(UNDO_TEXT);
			break;
		case OPEN_MENU:
			if (mainController.getTabPane().getTabs().size() == 0) {
				mainController.createNewTab(true);
				tabIndex = mainController.getCurrentTabIndex();
			}
			tabSpaces.get(tabIndex).sendEvent(OPEN_MENU);
			mainController.updateIsSaved(fileSaved);
			updateTitles();
			break;
		case REDO_TEXT:
			tabSpaces.get(tabIndex).sendEvent(REDO_TEXT);
			break;
		case SAVE_MENU:
			tabSpaces.get(tabIndex).sendEvent(SAVE_MENU);
			updateTitles();
			break;
		case ABOUT_MENU:
			break;
		case AUTO_SAVE:
			text = tabSpaces.get(tabIndex).getText();
			EditorUtils.writeToFile(text, filePath);
			updateTitles();
			break;
		case CLOSE_MENU:
			break;

		case EXIT_EVENT:
			text = tabSpaces.get(tabIndex).getText();
			EditorUtils.writeToFile(text, filePath);
			System.exit(0);
			break;

		case TAB_CHANGED:
			EditorUtils.setCurrentEditorTitle(mainController.getTabPane(), tabSpaces.get(tabIndex).getCurrentPath(),
					mainController.getCurrentTabIndex());
			break;
		case SAVE_REQUEST:
			EditorUtils.showSaveWindow(mainController.getTabPane().getScene().getWindow());
			break;
		case COPY_MENU:
			tabSpaces.get(tabIndex).sendEvent(COPY_MENU);
			break;
		case CUT_MENU:
			tabSpaces.get(tabIndex).sendEvent(CUT_MENU);
			break;
		case PASTE_MENU:
			tabSpaces.get(tabIndex).sendEvent(PASTE_MENU);
			break;
		case SHOW_FIND_REPLACE:
			tabSpaces.get(tabIndex).sendEvent(SHOW_FIND_REPLACE);
			break;
		case SHOW_FIND:
			tabSpaces.get(tabIndex).sendEvent(SHOW_FIND);
			break;
		case HIDE_REPLACE:
			tabSpaces.get(tabIndex).sendEvent(HIDE_REPLACE);
			break;
		case FIND_SELECT:
			tabSpaces.get(tabIndex).sendEvent(FIND_SELECT);
			break;
		case FIND_NEXT:
			tabSpaces.get(tabIndex).sendEvent(FIND_NEXT);
			break;
		case FIND_PREVIOUS:
			tabSpaces.get(tabIndex).sendEvent(FIND_PREVIOUS);
			break;
		case REPLACE_CURRENT:
			tabSpaces.get(tabIndex).sendEvent(REPLACE_CURRENT);
			break;
		case REPLACE_ALL:
			tabSpaces.get(tabIndex).sendEvent(REPLACE_ALL);
			break;
		case TREE_FILE:
			tabSpaces.get(tabIndex).sendEvent(Events.TREE_FILE);
			break;
		case VIEW:
			tabSpaces.get(tabIndex).sendEvent(Events.VIEW);
			break;
		default:
			break;

		}
	}

	/**
	 * updates the title of the tab, and stage whenever the selected tab changes
	 * does nothing if no file was opened
	 */
	private void updateTitles() {
		if (getFilePath() == null) {
			return;
		}
		EditorUtils.setTabTitle(mainController.getTabPane(), getFilePath(), mainController.getCurrentTabIndex());
		EditorUtils.setStageTitle(mainController.getTabPane(), getFilePath());
	}

	/**
	 * update Tab Title
	 * 
	 * @param index
	 */
	private void updateTabTitle(final int index) {
		final String title = mainController.getTabPane().getTabs().get(index).getText();
		if (title.charAt(title.length() - 1) == '*') {
			return;
		}
		mainController.getTabPane().getTabs().get(index).setText(title + " *");
	}

	/**
	 * getStageTitle
	 * 
	 * @return
	 */
	public Stage getStageTitle() {
		return EditorUtils.getStageTitle();
	}

	/**
	 * returns a builder of events, used to build events
	 */
	@Override
	public EventBuilder getEventBuilder() {
		return new EventBuilder(textChanged, fileSaved, filePath, text);
	}

	/**
	 * EventBuilder static sous class
	 */
	public static class EventBuilder {

		private boolean textChanged;
		private boolean fileSaved;
		private Path filePath;
		private String text;
		private Events event;

		/**
		 * EventBuilder constructor
		 * 
		 * @param textChanged
		 * @param fileSaved
		 * @param filePath
		 * @param text
		 */
		private EventBuilder(final boolean textChanged, final boolean fileSaved, final Path filePath,
				final String text) {
			this.textChanged = textChanged;
			this.fileSaved = fileSaved;
			this.filePath = filePath;
			this.text = text;
		}

		/**
		 * textChanged event
		 * 
		 * @param textChanged
		 * @return
		 */
		public EventBuilder textChanged(final boolean textChanged) {
			this.textChanged = textChanged;
			return this;
		}

		/**
		 * fileSaved event
		 * 
		 * @param fileSaved
		 * @return
		 */
		public EventBuilder fileSaved(final boolean fileSaved) {
			this.fileSaved = fileSaved;
			return this;
		}

		/**
		 * withFilePath event
		 * 
		 * @param filePath
		 * @return
		 */
		public EventBuilder withFilePath(final Path filePath) {
			this.filePath = filePath;
			return this;
		}

		/**
		 * withText event
		 * 
		 * @param text
		 * @return
		 */
		public EventBuilder withText(final String text) {
			this.text = text;
			return this;
		}

		/**
		 * withEvent
		 * 
		 * @param event
		 * @return
		 */
		public EventBuilder withEvent(final Events event) {
			this.event = event;
			return this;
		}

		/**
		 * build
		 */
		public void build() {
			Mediator mediator = Mediator.getInstance();
			mediator.text = this.text;
			mediator.filePath = this.filePath;
			mediator.fileSaved = this.fileSaved;
			mediator.textChanged = this.textChanged;
			mediator.notify(event);
		}
	}

	/**
	 * @return the textChanged
	 */
	public boolean isTextChanged() {
		return textChanged;
	}

	/**
	 * @param textChanged the textChanged to set
	 */
	public void setTextChanged(boolean textChanged) {
		this.textChanged = textChanged;
	}

	/**
	 * @return the xslFilePath
	 */
	public String getXslFilePath() {
		return xslFilePath;
	}

	/**
	 * @param xslFilePath the xslFilePath to set
	 */
	public void setXslFilePath(String xslFilePath) {
		this.xslFilePath = xslFilePath;
	}

	@Override
	public WebEngine getWebEngine() {
		return this.webEngine;
	}

	@Override
	public void setWebEngine(WebEngine engine) {
		this.webEngine = engine;

	}
}