package io.github.alantcote.browsersfour.settings;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

/**
 * The superclass of settings detail editor panes.
 */
public abstract class Editor extends BorderPane {
	/**
	 * True if the displayed values differ from the initial values.
	 */
	protected SimpleBooleanProperty dirty = new SimpleBooleanProperty(false);

	/**
	 * Construct a new object.
	 */
	public Editor() {
	}

	/**
	 * Construct a new object.
	 * 
	 * @param center the component to be used in the center.
	 */
	public Editor(Node center) {
		super(center);
	}

	/**
	 * Construct a new object.
	 * 
	 * @param center the component to be used in the center.
	 * @param top    the component to be used at the top.
	 * @param right  the component to be used on the right.
	 * @param bottom the component to be used at the bottom.
	 * @param left   the component to be used on the left.
	 */
	public Editor(Node center, Node top, Node right, Node bottom, Node left) {
		super(center, top, right, bottom, left);
	}

	/**
	 * Store the displayed values into the backing settings.
	 * 
	 * Validate the displayed values before committing. If displayed values are in
	 * error in some way, a suitable message should be displayed and
	 * <code>false</code> should be returned.
	 * 
	 * @return <code>true</code> if the commit succeeds, else <code>false</code>.
	 */
	public abstract boolean commit();

	public SimpleBooleanProperty dirtyProperty() {
		return dirty;
	}

	public boolean isDirty() {
		return dirty.get();
	}

	/**
	 * Reset the displayed values from the backing settings.
	 */
	public abstract void reset();

	public void setDirty(boolean val) {
		dirty.set(val);
	}
}
