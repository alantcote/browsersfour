package io.github.alantcote.browsersfour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * A manager for a list of URLs as strings.
 */
public class FavoritesManager {
	public static final ButtonType APPLY_BUTTON_TYPE = new ButtonType("Apply", ButtonData.OK_DONE);
	public static final ButtonType CANCEL_BUTTON_TYPE = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	public static final String DEFAULT_FAVORITE = "x";
	public static final String DIALOG_TITLE = "Favorites";
	public static final String FAVORITES_PREF_PATH = "options/favorites";
	public static final String URL_PREF_KEY_PREFIX = "url";
	
	protected ObservableList<String> editingFavorites = FXCollections.observableArrayList();

	protected ObservableList<String> favorites = FXCollections.observableArrayList();

	protected Preferences favoritesPrefs = null;
	
	/**
	 * Construct a new object.
	 * 
	 * @param appPrefs the application's {@link Preferences} node.
	 */
	public FavoritesManager(Preferences appPrefs) {
		favoritesPrefs = appPrefs.node(FAVORITES_PREF_PATH);

		updateFavorites();

		favoritesPrefs.addPreferenceChangeListener(new PreferenceChangeListener() {
			@Override
			public void preferenceChange(PreferenceChangeEvent evt) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						updateFavorites();
					}
				});
			}
		});
	}
	
	public void edit() {
		Dialog<ButtonType> editorDialog = createEditorDialog();
		
		Optional<ButtonType> result = editorDialog.showAndWait();
		
		if ((result.isPresent()) && (result.get() == APPLY_BUTTON_TYPE)) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					favorites.clear();
					favorites.addAll(editingFavorites);
					
					updateFavoritesPrefs();
				}
			});
		}
	}
	
	/**
	 * @return the favorites
	 */
	public ObservableList<String> getFavorites() {
		return favorites;
	}
	protected MenuItem createAddFavoriteMenuItem() {
		MenuItem menuItem = new MenuItem("Add favorite");
		
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				editingFavorites.add("new favorite");
			}
			
		});
		
		return menuItem;
	}
	
	protected Callback<ListView<String>, ListCell<String>> createCellFactory() {
		Callback<ListView<String>, ListCell<String>> factory = new Callback<ListView<String>, ListCell<String>>() {

			@Override
			public ListCell<String> call(ListView<String> param) {
				TextFieldListCell<String> listCell = new TextFieldListCell<String>();

				listCell.setEditable(true);
				listCell.setContextMenu(createContextMenu(listCell));
				listCell.setConverter(new StringConverter<String>() {

					@Override
					public String toString(String object) {
						return object;
					}

					@Override
					public String fromString(String string) {
						return string;
					}
					
				});
				
				return listCell;
			}
			
		};
		
		return factory;
	}
	
	protected ContextMenu createContextMenu(TextFieldListCell<String> listCell) {
		ContextMenu contextMenu = new ContextMenu();
		ObservableList<MenuItem> menuItems = contextMenu.getItems();
		
		menuItems.add(createAddFavoriteMenuItem());
		menuItems.add(createDeleteFavoriteMenuItem(listCell));
		
		return contextMenu;
	}

	protected MenuItem createDeleteFavoriteMenuItem(TextFieldListCell<String> listCell) {
		MenuItem menuItem = new MenuItem("Delete favorite");
		
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				int index = listCell.getIndex();
				
				editingFavorites.remove(index);
			}
			
		});
		
		return menuItem;
	}
	
	protected Dialog<ButtonType> createEditorDialog() {
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		DialogPane dialogPane = dialog.getDialogPane();
		ObservableList<ButtonType> buttonTypes = dialogPane.getButtonTypes();
		
		dialog.setTitle(DIALOG_TITLE);
		dialogPane.setContent(createScrollableListView());
		buttonTypes.add(APPLY_BUTTON_TYPE);
		buttonTypes.add(CANCEL_BUTTON_TYPE);
		
		return dialog;
	}
	
	protected ListView<String> createListView() {
		editingFavorites.clear();
		editingFavorites.addAll(favorites);
		
		ListView<String> listView = new ListView<String>(editingFavorites);
		
		listView.setCellFactory(createCellFactory());
		listView.setPrefWidth(1200);
		listView.setEditable(true);
		
		return listView;
	}

	protected ScrollPane createScrollableListView() {
		ListView<String> listView = createListView();
		ScrollPane scrollPane = new ScrollPane(listView);
		
		scrollPane.setPrefSize(400, 400);
		
		return scrollPane;
	}
	
	protected void updateFavoritesPrefs() {
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
			System.err.println("FavoritesManager.updateFavoritesPrefs(): caught: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Update {@link @favorites} from {@link FavoritesManager#favoritesPrefs}.
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
}
