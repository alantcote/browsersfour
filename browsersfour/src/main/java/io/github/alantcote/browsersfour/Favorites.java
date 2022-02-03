package io.github.alantcote.browsersfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A manager for a list of URLs as strings.
 */
public class Favorites {
	public static final String DEFAULT_FAVORITE = "x";
	public static final String FAVORITES_PREF_PATH = "options/favorites";
	public static final String URL_PREF_KEY_PREFIX = "url";

	protected ObservableList<String> favorites = FXCollections.observableArrayList();

	protected Preferences favoritesPrefs = null;
	
	protected boolean updatingPreferencesNode = false;

	/**
	 * Construct a new object.
	 * 
	 * @param appPrefs the application's {@link Preferences} node.
	 */
	public Favorites(Preferences appPrefs) {
		favoritesPrefs = appPrefs.node(FAVORITES_PREF_PATH);

		favoritesPrefs.addPreferenceChangeListener(new PreferenceChangeListener() {
			@Override
			public void preferenceChange(PreferenceChangeEvent evt) {
				if (!updatingPreferencesNode) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							updateFavorites();
						}
					});
				}
			}
		});

		updateFavorites();
	}

	/**
	 * @return the favorites
	 */
	public ObservableList<String> getFavorites() {
		return favorites;
	}

	/**
	 * Update {@link #favorites} from {@link Favorites#favoritesPrefs}.
	 */
	protected void updateFavorites() {
		if (favoritesPrefs == null) {
			return;
		}

		ArrayList<String> urlArrayList = new ArrayList<String>();
		int index = 0;

		while (true) {
			String key = URL_PREF_KEY_PREFIX + index;
			String url = favoritesPrefs.get(key, DEFAULT_FAVORITE);

			if (DEFAULT_FAVORITE.equals(url)) {
				break;
			}

			urlArrayList.add(url);

			++index;
		}

		Collections.sort(urlArrayList);

		favorites.clear();
		favorites.addAll(urlArrayList);
	}

	protected void updateFavoritesPrefs() {
		updatingPreferencesNode = true;
		int index = 0;

		try {
			favoritesPrefs.clear();

			for (String fave : favorites) {
				String key = URL_PREF_KEY_PREFIX + index;

				favoritesPrefs.put(key, fave);

				++index;
			}

			favoritesPrefs.sync();
		} catch (BackingStoreException e) {
			System.err.println("Favorites.updateFavoritesPrefs(): caught: " + e.getMessage());
			e.printStackTrace();
		}
		
		updatingPreferencesNode = false;
	}
}
