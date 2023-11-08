package gui;

import java.nio.file.Path;

import gui.components.FindReplaceToolBar;
import gui.components.TextSpace;
import gui.mediator.Events;
import gui.mediator.Mediator;
import javafx.scene.input.Clipboard;
import smallUndoEngine.EditorTextHistory;

/**
 * a class that wraps TextSpace, EditorTextHistory and FindReplaceToolBar
 * objects together
 * 
 * @author bensa Nizar
 */
public class TabSpace {

	private Mediator mediator = Mediator.getInstance();
	private TextSpace textSpace;
	private EditorTextHistory editorTextHistory;
	private FindReplaceToolBar findReplaceToolBar;
	private String toolBarString;
	private boolean fileSaved;
	private boolean textChanged;

	/**
	 * TabSpace
	 * 
	 * @param textSpace
	 * @param editorTextHistory
	 * @param findReplaceToolBar
	 */
	public TabSpace(final TextSpace textSpace, final EditorTextHistory editorTextHistory,
			final FindReplaceToolBar findReplaceToolBar) {
		this.textSpace = textSpace;
		this.editorTextHistory = editorTextHistory;
		this.findReplaceToolBar = findReplaceToolBar;
	}

	/**
	 * updates fileSaved
	 *
	 * @param bool the value isSaved will be set to
	 */
	public void setIsSaved(final boolean bool) {
		fileSaved = bool;
	}

	/**
	 * replaces textArea's text with itself appended with string content in the
	 * clipboards to textArea by
	 */
	private void pasteToTextArea() {
		final StringBuilder sb = new StringBuilder();
		final String clipboardString = Clipboard.getSystemClipboard().getString();
		final String finalText = sb.append(getText()).append(" ").append(clipboardString).toString();
		textSpace.setText(finalText);
	}

	/**
	 * removes the selected text in textArea
	 */
	private void cutFromTextArea() {
		textSpace.removeSelectedText();
	}

	/**
	 * sendEvent
	 * @param event
	 */
	public void sendEvent(final Events event) {
		switch (event) {
		case UNDO_TEXT:
			textSpace.undo(editorTextHistory);
			textChanged = true;
			break;

		case REDO_TEXT:
			textSpace.redo(editorTextHistory);
			textChanged = true;
			break;

		case OPEN_MENU:
			textSpace.setCurrentPath(mediator.getMediatorFilePath());
			textSpace.setText(mediator.getMediatorText());
			fileSaved = true;
			break;

		case SAVE_MENU:
			textSpace.setCurrentPath(mediator.getMediatorFilePath());
			fileSaved = true;
			textChanged = false;
			break;

		case TEXT_CHANGED:
			editorTextHistory.update(textSpace.getText());
			textChanged = true;
			break;

		case COPY_MENU:
			mediator.setText(textSpace.getSelectedText());
			break;

		case CUT_MENU:
			mediator.setText(textSpace.getSelectedText());
			cutFromTextArea();
			break;

		case PASTE_MENU:
			pasteToTextArea();
			break;

		case SHOW_FIND_REPLACE:
			findReplaceToolBar.showFindReplace();
			break;

		case SHOW_FIND:
			findReplaceToolBar.showFindToolbar();
			break;

		case HIDE_REPLACE:
			findReplaceToolBar.hideFindReplace();
			// remove highlighting
			textSpace.resetIndicesTracker();
			textSpace.selectText("");
			break;

		case FIND_SELECT:
			toolBarString = mediator.getMediatorText();
			textSpace.resetIndicesTracker();
			textSpace.selectText(toolBarString);
			break;

		case FIND_NEXT:
			textSpace.increaseIndicesTracker();
			textSpace.selectText(toolBarString);
			break;

		case FIND_PREVIOUS:
			textSpace.decreaseIndicesTracker();
			textSpace.selectText(toolBarString);
			break;

		case REPLACE_CURRENT:
			textSpace.replaceCurrent(toolBarString, mediator.getMediatorText());
			break;

		case REPLACE_ALL:
			textSpace.replaceAll(toolBarString, mediator.getMediatorText());
			break;
		case TREE_FILE:
			textSpace.replaceAll(toolBarString, mediator.getMediatorText());
			break;
		case VIEW:
			textSpace.replaceAll(toolBarString, mediator.getMediatorText());
			break;
		default:
			break;
		}

	}

	/**
	 * getText
	 * 
	 * @return
	 */
	public String getText() {
		return textSpace.getText();
	}

	/**
	 * getCurrentPath
	 * 
	 * @return
	 */
	public Path getCurrentPath() {
		return textSpace.getCurrentPath();
	}

	/**
	 * isFileSaved
	 * 
	 * @return
	 */
	public boolean isFileSaved() {
		return fileSaved;
	}

	/**
	 * isTextChanged
	 * 
	 * @return
	 */
	public boolean isTextChanged() {
		return textChanged;
	}

}