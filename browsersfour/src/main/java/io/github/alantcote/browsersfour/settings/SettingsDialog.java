package io.github.alantcote.browsersfour.settings;

import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * A settings dialog.
 */
public class SettingsDialog extends Stage {

	/**
	 * Construct a new object.
	 * @param rootTreeItem the root item for the {@link TreeView}.
	 */
	public SettingsDialog(CategoryTreeItem rootTreeItem) {
		this(StageStyle.DECORATED, rootTreeItem);
	}

	/**
	 * Construct a new object.
	 * @param style the style of the Stage.
	 * @param rootTreeItem the root item for the {@link TreeView}.
	 */
	public SettingsDialog(StageStyle style, CategoryTreeItem rootTreeItem) {
		super(style);
		
		SettingsPane settingsPane = new SettingsPane(rootTreeItem);
		Scene scene = new Scene(settingsPane, 1000, 800);
		
		setScene(scene);
	}
}
