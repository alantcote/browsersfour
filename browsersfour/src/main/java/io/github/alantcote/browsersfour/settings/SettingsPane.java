package io.github.alantcote.browsersfour.settings;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;

/**
 * The root pane for a settings dialog.
 */
public class SettingsPane extends BorderPane {
	protected Button applyButton = new Button("Apply");
	protected ButtonBar buttonBar;
	protected CategoryTreeView categoryTreeView;
	protected BorderPane detailsPane;
	protected Editor editor = null;
	protected Button revertButton = new Button("Revert");
	protected CategoryTreeItem rootItem;
	protected ReadOnlyObjectProperty<TreeItem<Category>> selectedItemProperty;

	public SettingsPane(CategoryTreeItem rootTreeItem) {
		super();

		rootItem = rootTreeItem;

		createHierarchy();
	}

	protected void createApplyButton() {
		applyButton = new Button("Apply");

		applyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (editor != null) {
					editor.commit();
				}
			}
		});
	}

	protected void createButtonBar() {
		buttonBar = new ButtonBar();

		ObservableList<Node> buttons = buttonBar.getButtons();

		createApplyButton();
		buttons.add(applyButton);

		createRevertButton();
		buttons.add(revertButton);
	}

	protected void createHierarchy() {
		createTreeView();
		setLeft(categoryTreeView);

		detailsPane = new BorderPane();

		createButtonBar();
		detailsPane.setBottom(buttonBar);
		
		setCenter(detailsPane);
	}

	protected void createRevertButton() {
		revertButton = new Button("Revert");

		revertButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (editor != null) {
					editor.reset();
				}
			}
		});
	}

	protected void createTreeView() {
		categoryTreeView = new CategoryTreeView(rootItem);

//		categoryTreeView.setShowRoot(false);
//		categoryTreeView.setCellFactory(new CategoryTreeCellFactory());
//		categoryTreeView.setEditable(true);
//		categoryTreeView.setPrefHeight(USE_COMPUTED_SIZE);
		MultipleSelectionModel<TreeItem<Category>> selModel = categoryTreeView.getSelectionModel();
//		
//		selModel.selectionModeProperty().set(SelectionMode.MULTIPLE);
//		selModel.selectionModeProperty().set(SelectionMode.SINGLE);
//		
		selectedItemProperty = selModel.selectedItemProperty();

		selectedItemProperty.addListener(new ChangeListener<TreeItem<Category>>() {

			@Override
			public void changed(ObservableValue<? extends TreeItem<Category>> observable, TreeItem<Category> oldValue,
					TreeItem<Category> newValue) {
				detailsPane.setCenter(newValue.getValue().getEditor());
			}
			
		});
	}
}
