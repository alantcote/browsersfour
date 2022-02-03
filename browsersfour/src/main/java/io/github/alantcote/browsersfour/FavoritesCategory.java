package io.github.alantcote.browsersfour;

import io.github.alantcote.browsersfour.settings.Category;
import io.github.alantcote.browsersfour.settings.CategoryTreeItem;
import io.github.alantcote.browsersfour.settings.Editor;

/**
 * A settings category for favorites.
 */
public class FavoritesCategory implements Category {
	public static final String CATEGORY_NAME = "Favorites";
	protected Favorites favorites;

	/**
	 * Construct a new object.
	 * 
	 * @param faves the application's {@link Favorites} object.
	 */
	public FavoritesCategory(Favorites faves) {
		favorites = faves;
	}

	@Override
	public Editor getEditor() {
		return new FavoritesEditor(favorites);
	}

	@Override
	public String getName() {
		return CATEGORY_NAME;
	}

	public CategoryTreeItem newTreeItem() {
		return new CategoryTreeItem(this);
	}
}
