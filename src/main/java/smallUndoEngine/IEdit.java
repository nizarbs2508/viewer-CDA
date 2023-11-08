package smallUndoEngine;

/**
 * IEdit interface
 * 
 * @author bensa Nizar
 */
public interface IEdit {
	/**
	 * undo
	 */
	void undo();

	/**
	 * redo
	 */
	void redo();
}
