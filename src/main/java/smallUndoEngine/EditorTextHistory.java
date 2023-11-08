package smallUndoEngine;

/**
 * acts as a connector between the MainController class and the Edit class
 * 
 * @author bensa Nizar
 */
public class EditorTextHistory implements IEdit {

	private Edit edit = new Edit();

	/**
	 * EditorTextHistory
	 * 
	 * @param text
	 */
	public EditorTextHistory(final String text) {
		edit.setText(text);
	}

	/**
	 * EditorTextHistory
	 */
	public EditorTextHistory() {

	}

	/**
	 * getText
	 * 
	 * @return
	 */
	public String getText() {
		return edit.getText();
	}

	/**
	 * update
	 * 
	 * @param newText
	 */
	public void update(final String newText) {
		edit.setText(newText);
	}

	/**
	 * undo
	 */
	public void undo() {
		edit.undo();
	}

	/**
	 * redo
	 */
	public void redo() {
		edit.redo();
	}
}