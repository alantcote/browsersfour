package io.github.alantcote.browsersfour;

import java.util.prefs.Preferences;

import io.github.alantcote.browsersfour.settings.Category;
import io.github.alantcote.browsersfour.settings.CategoryTreeItem;
import io.github.alantcote.browsersfour.settings.Editor;

/**
 * A settings category for favorites.
 */
public class FavoritesCategory implements Category {
	public static final String CATEGORY_NAME = "Favorites";
	protected Preferences appPrefs;

	/**
	 * Construct a new object.
	 * 
	 * @param appPrefs the application's {@link Preferences} node.
	 */
	public FavoritesCategory(Preferences appPrefs) {
		this.appPrefs = appPrefs;
	}

	@Override
	public Editor getEditor() {
		return new FavoritesEditor(appPrefs);
	}

	@Override
	public String getName() {
		return CATEGORY_NAME;
	}

	public CategoryTreeItem newTreeItem() {
		return new CategoryTreeItem(this);
	}
}
