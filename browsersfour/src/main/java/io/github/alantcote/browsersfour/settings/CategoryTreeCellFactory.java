package io.github.alantcote.browsersfour.settings;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

/**
 * A factory for {@link CategoryTreeCell} objects.
 */
public class CategoryTreeCellFactory implements Callback<TreeView<Category>, TreeCell<Category>> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public TreeCell<Category> call(TreeView<Category> param) {
		return new CategoryTreeCell();
	}
}
