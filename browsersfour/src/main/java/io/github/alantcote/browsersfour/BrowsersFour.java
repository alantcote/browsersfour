package io.github.alantcote.browsersfour;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import io.github.alantcote.browsersfour.settings.CategoryTreeItem;
import io.github.alantcote.browsersfour.settings.SettingsDialog;
import io.github.alantcote.clutilities.javafx.windowprefs.WindowPrefs;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BrowsersFour extends Application {
	protected class Updater implements Runnable {
		protected int browserIndex = 0;

		@Override
		public void run() {
			String url = idleRandomFavorite();

			if (url != null) {
//				System.err.println("Updater: loading " + url);
				
				browser[browserIndex++].load(url);
			}

			if (browserIndex >= BROWSER_COUNT) {
				browserIndex = 0;
			}
		}
	}

	public static final int BROWSER_COUNT = 4;
	public static long UPDATE_PERIOD = 5000; // update period im msecs

	/**
	 * The entry point for the application.
	 * 
	 * @param args command-line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	protected Preferences appPrefs = null;
	protected BrowserPanel[] browser = new BrowserPanel[BROWSER_COUNT];
	protected Favorites favorites = null;
	protected Stage b4Stage;
	protected Ticker ticker = new Ticker(new Updater(), UPDATE_PERIOD);
	protected WindowPrefs windowPrefs = null;

	/**
	 * @return the appPrefs
	 */
	public Preferences getAppPrefs() {

		if (appPrefs == null) {
			appPrefs = windowPrefs.getPrefs();
		}

		return appPrefs;
	}
	protected Stage stage;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(Stage primaryStage) {
		b4Stage = primaryStage;
		
		try {
			GridPane gridPane = inizBrowserPanels();
			BorderPane root = new BorderPane();
			Scene scene = newScene(root, 1000, 800);

			root.setUserData(getHostServices()); // covert communication to controller
			root.setTop(createMenuBar());
			root.setCenter(gridPane);

			stageSetScene(primaryStage, scene);
			stageSetTitle(primaryStage);

			try {
				windowPrefs = new WindowPrefs(BrowsersFour.class, primaryStage);
				
				appPrefs = windowPrefs.getPrefs();
			} catch (BackingStoreException e) {
				System.err.println("PathTreeViewDemo.start(): caught: " + e.getMessage());
				e.printStackTrace();
				System.err.println("PathTreeViewDemo.start(): continuing . . .");
			}

			stageShow(primaryStage);

			favorites = new Favorites(windowPrefs.getPrefs());

			for (int i = 0; i < BROWSER_COUNT; ++i) {
				browser[i].load(idleRandomFavorite());
			}

			ticker.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected Menu createEditMenu() {
		Menu menu = new Menu("Edit");
		
//		menu.getItems().add(createFavoritesItem());
		menu.getItems().add(createSettingsItem());
		
		return menu;
	}
	
	protected MenuItem createSettingsItem() {
		MenuItem menuItem = new MenuItem("Settings");
		
		menuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				CategoryTreeItem root = CategoryTreeItem.createRootItem();
				FavoritesCategory favesCat = new FavoritesCategory(favorites);
				CategoryTreeItem favesItem = favesCat.newTreeItem();
				
				root.getChildCategories().add(favesItem);
				
				SettingsDialog dialog = new SettingsDialog(root);
				
				dialog.initOwner(b4Stage);
				dialog.showAndWait();
			}
			
		});
		
		return menuItem;
	}
	
//	protected MenuItem createFavoritesItem() {
//		MenuItem menuItem = new MenuItem("Favorites");
//		
//		menuItem.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				ticker.stop();
//				favorites.edit();
//				ticker.start();
//			}
//			
//		});
//		
//		return menuItem;
//	}
	
	protected MenuBar createMenuBar() {
		MenuBar menuBar = new MenuBar();
		
		menuBar.getMenus().add(createEditMenu());
		
		return menuBar;
	}

	/**
	 * Get a favorite that is not displayed in any browser pane.
	 * 
	 * It is possible that the value returned corresponds to one that is displayed
	 * if there are not enough favorites to populate all the browser panels
	 * uniquely.
	 * 
	 * @return the favorite.
	 */
	protected String idleRandomFavorite() {
		String result = null;
		ObservableList<String> faves = favorites.getFavorites();
		int urlCount = faves.size();
		boolean inUse = false;

		if (urlCount > 0) {
			do {
				int chosen = (int) (Math.random() * urlCount);

				result = faves.get(chosen);
				inUse = false;

				if (urlCount > BROWSER_COUNT) {
					for (int i = 0; i < BROWSER_COUNT; ++i) {
						if (result.equals(browser[i].getURL())) {
							inUse = true;
						}
					}
				}
			} while (inUse);
		}

		return result;
	}

	/**
	 * Initialize the browser panels.
	 * 
	 * @return the parent container.
	 */
	protected GridPane inizBrowserPanels() {
		GridPane gridPane = new GridPane();

		gridPane.setGridLinesVisible(true);

		for (int i = 0; i < 4; ++i) {
			browser[i] = newBrowserPanel();

			gridPane.add(browser[i], i % 2, i / 2);
		}

		return gridPane;
	}

	/**
	 * @return a new {@link BorderPane}.
	 */
	protected BrowserPanel newBrowserPanel() {
		return new BrowserPanel();
	}

	/**
	 * Manufacture a new {@link Scene}.
	 * 
	 * @param root the root pane to wrap.
	 * @return the new object.
	 */
	protected Scene newScene(BorderPane root, double width, double height) {
		return new Scene(root, width, height);
	}

	/**
	 * Set a stage's scene.
	 * 
	 * @param stage the stage on which to set the scene.
	 * @param scene the scene.
	 */
	protected void stageSetScene(Stage stage, Scene scene) {
		stage.setScene(scene);
	}

	/**
	 * Set a stage (window0 title.
	 * 
	 * @param stage the stage on which to set the title.
	 */
	protected void stageSetTitle(Stage stage) {
		stage.setTitle("Browsers Four");
	}

	/**
	 * Show a stage.
	 * 
	 * @param stage the stage to show.
	 */
	protected void stageShow(Stage stage) {
		stage.show();
	}
}
