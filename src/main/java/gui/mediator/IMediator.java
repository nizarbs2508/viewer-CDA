package gui.mediator;

import java.nio.file.Path;
import java.util.List;

import gui.TabSpace;
import gui.components.FindReplaceToolBar;
import gui.components.MainController;
import gui.components.MainMenuBar;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

/**
 * interface IMediator
 * 
 * @author bensa Nizar
 */
public interface IMediator {
	void setMenuBar(MainMenuBar mainMenuBar);

	void setTabSpaces(List<TabSpace> tabSpaces);

	void setMainController(MainController mainController);

	void setFindReplaceToolBar(FindReplaceToolBar findReplaceToolBar);

	String getText();

	Path getFilePath();

	boolean isFileSaved();

	boolean shouldExit();

	boolean isMatchCase();

	Mediator.EventBuilder getEventBuilder();

	String getMediatorText();

	Path getMediatorFilePath();

	Stage getStageTitle();
	
	String getXslFilePath();
	
	void setXslFilePath(String xslFilePath);
	
	WebEngine getWebEngine();
	
	void setWebEngine(WebEngine engine);
}
