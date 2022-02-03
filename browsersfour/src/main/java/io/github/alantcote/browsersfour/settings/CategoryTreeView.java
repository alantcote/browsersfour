package io.github.alantcote.browsersfour.settings;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * A {@link TreeView} used to select categories in a {@link SettingsDialog}.
 */
public class CategoryTreeView extends TreeView<Category> {
	/**
	 * Construct a new object.
	 */
	public CategoryTreeView() {
		finishConstruction();
	}

	/**
	 * Construct a new object.
	 * @param root the root of the category tree.
	 */
	public CategoryTreeView(TreeItem<Category> root) {
		super(root);

		finishConstruction();
	}
	
	protected void finishConstruction() {
		setCellFactory(new CategoryTreeCellFactory());
		setShowRoot(false);
	}
}
