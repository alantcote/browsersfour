package io.github.alantcote.browsersfour.settings;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * A settings category.
 * 
 * Settings categories are the payloads of {@link TreeItem} objects in the
 * {@link TreeView} that appears in the left-hand pane of a settings dialog.
 * 
 * Settings categories also provide the {@link Editor} panes that enable users
 * to edit the detailed settings associated with the category.
 */
public interface Category {
	/**
	 * Get an editor for the settings associated with this category.
	 * 
	 * This is called by the settings dialog when the corresponding
	 * {@link TreeItem} is selected in the {@link TreeView}. The {@link Editor}
	 * is then wrapped in a {@link ScrollPane} which in turn is made the upper
	 * right-hand pane of the settings dialog.
	 * @return an {@link Editor}. A value of <code>null</code> yields a blank
	 * pane.
	 */
	public Editor getEditor();
	
	/**
	 * Get the category name.
	 * 
	 * @return the category name.
	 */
	public String getName();
}
