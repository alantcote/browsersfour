package io.github.alantcote.browsersfour.settings;

import java.util.Comparator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * A {@link TreeItem} for use in the {@link TreeView} displayed in the left-hand
 * pane of a settings dialog.
 * 
 * Child items may be added by
 * {@link #getChildCategories()}<code>.add(childItem)</code>.
 */
public class CategoryTreeItem extends TreeItem<Category> {
	/**
	 * Create a new root item of this class.
	 */
	public static CategoryTreeItem createRootItem() {
		return new CategoryTreeItem(new RootCategory());
	}

	/**
	 * This item's child items.
	 */
	protected ObservableList<TreeItem<Category>> childCategories = null;

	/**
	 * A {@link Comparator} for objects of this class.
	 */
	protected Comparator<TreeItem<Category>> comparator = new Comparator<TreeItem<Category>>() {
		@Override
		public int compare(TreeItem<Category> o1, TreeItem<Category> o2) {
			return o1.getValue().getName().compareTo(o2.getValue().getName());
		}
	};

	/**
	 * Construct a new object.
	 * 
	 * @param value the {@link Category} payload of the new object.
	 */
	public CategoryTreeItem(Category value) {
		super(value);

		childCategories = super.getChildren();
	}

	/**
	 * Construct a new object.
	 * 
	 * @param value   the {@link Category} payload of the new object.
	 * @param graphic the {@link Node} to be displayed with the {@link Category}
	 *                name in the {@link TreeView}.
	 */
	public CategoryTreeItem(Category value, Node graphic) {
		super(value, graphic);

		childCategories = super.getChildren();
	}

	/**
	 * @return the childCategories
	 */
	public ObservableList<TreeItem<Category>> getChildCategories() {
		FXCollections.sort(childCategories, comparator);

		return childCategories;
	}

	@Override
	public ObservableList<TreeItem<Category>> getChildren() {
		return childCategories;
	}

	@Override
	public boolean isLeaf() {
		return 0 == childCategories.size();
	}
}
