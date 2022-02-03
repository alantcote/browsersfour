package io.github.alantcote.browsersfour.settings;

import javafx.scene.control.TreeCell;

/**
 * A {@link TreeCell} for displaying {@link Category} objects.
 */
public class CategoryTreeCell extends TreeCell<Category> {
	/**
	 * Construct a new object.
	 */
	public CategoryTreeCell() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateItem(Category item, boolean empty) {
		super.updateItem(item, empty);
		
		setGraphic(null);
		
		if (empty) {
			setText(null);
		} else {
			setText(item.getName());
		}
	}
}
