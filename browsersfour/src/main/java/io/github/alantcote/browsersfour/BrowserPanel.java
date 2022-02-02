package io.github.alantcote.browsersfour;

import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.web.WebView;

/**
 * A web browser component.
 */
public class BrowserPanel extends BorderPane {
	protected BorderPane controlPane = new BorderPane();
	protected TextField urlView = new TextField();
	protected WebView webView = new WebView();
	protected ToggleButton pauseToggle = new ToggleButton("Pause");
	protected TextField titleField = new TextField();

	/**
	 * Construct a new object.
	 */
	public BrowserPanel() {
		urlView.setEditable(false);
		
		Font baseFont = titleField.getFont();
		String fontFamily = baseFont.getFamily();
		double size = baseFont.getSize();
		Font newFont = Font.font(fontFamily, FontWeight.BOLD, size);
		
		titleField.setFont(newFont);
		titleField.textProperty().bind(webView.getEngine().titleProperty());
		
		controlPane.setCenter(urlView);
		controlPane.setBottom(titleField);
		
		pauseToggle.setSelected(false);
		
		controlPane.setRight(pauseToggle);
		
		setTop(controlPane);
		
		setCenter(webView);
	}
	
	protected boolean isPaused() {
		return pauseToggle.isSelected();
	}

	/**
	 * Load a web page
	 * 
	 * @param url the URL of the page to load.
	 */
	public void load(String url) {
		if (!isPaused()) {
			urlView.setText(url);
			webView.getEngine().load(url);
		}
	}
	
	public String getURL() {
		return urlView.getText();
	}
}
