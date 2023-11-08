package gui.components;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import com.ans.cda.Constant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * About main menu Controller
 * 
 * @author bensa Nizar
 */
public class AboutController {

	@FXML
	private Hyperlink ansLink;

	@FXML
	private Button okButton;

	@FXML
	private Text aboutText;

	/**
	 * OK Button close
	 * 
	 * @param event
	 */
	@FXML
	void okButtonClicked(final ActionEvent event) {
		final Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
	}

	/**
	 * open ans Link
	 * 
	 * @param event
	 */
	@FXML
	void ansLinkOnAction(final ActionEvent event) {

		final String githubURL = Constant.link;
		try {
			if (Desktop.isDesktopSupported()) {
				final Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(URI.create(githubURL));
				}
			}

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}