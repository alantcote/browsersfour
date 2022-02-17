/**
 * 
 */
package io.github.alantcote.browsersfour;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLAnchorElement;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.SAXException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Importer for favorites.
 * 
 * This really should be done in a Worker thread.
 */
public class FavoritesImporter {
	protected Stage primaryStage;

	/**
	 * Constructor.
	 */
	public FavoritesImporter() {
		this(null);
	}

	public FavoritesImporter(Stage parentStage) {
		primaryStage = parentStage;
	}

	/**
	 * Import favorites from an HTML file chosen by the user.
	 * 
	 * @return the list of favorites or <code>null</code> if no favorites were
	 *         imported.
	 */
	public ObservableList<String> importFromHTML() {
		ObservableList<String> favorites = null;
		File selectedFile = chooseFile();

		favorites = importFromHTML(selectedFile);

		return favorites;
	}

	/**
	 * Import favorites from an HTML file.
	 * 
	 * @param file the HTML file.
	 * @return the list of favorites or <code>null</code> if no favorites were
	 *         imported.
	 */
	public ObservableList<String> importFromHTML(File file) {
		ObservableList<String> favorites = null;
		
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
//			FileInputStream inputStream = new FileInputStream(file);
			Document doc = builder.parse(file);
			
			if ((null != doc) && (doc instanceof HTMLDocument)) {
				HTMLDocument htmlDoc = (HTMLDocument) doc;
				HTMLCollection anchors = htmlDoc.getAnchors();
				int nbrAnchors = anchors.getLength();
				
				for (int anchorIndex = 0; anchorIndex < nbrAnchors; ++anchorIndex) {
					Node anchor = anchors.item(anchorIndex);
					
					if (anchor instanceof HTMLAnchorElement) {
						HTMLAnchorElement htmlAnchor = (HTMLAnchorElement) anchor;
						String href = htmlAnchor.getHref();
						
						if (href != null) {
							if (favorites == null) {
								favorites = FXCollections.observableArrayList();
							}
							
							favorites.addAll(href);
							
							System.out.println("FavoritesImporter.importFromHTML(): href = \"" + href + "\"");
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return favorites;
	}

	/**
	 * Choose the file from which to import favorites.
	 * 
	 * @return the file or null if no file was chosen.
	 */
	protected File chooseFile() {
		FileChooser fileChooser = new FileChooser();

		fileChooser.setTitle("Choose Favorites File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("All Files", "*.*"));

		File selectedFile = fileChooser.showOpenDialog(primaryStage);

		return selectedFile;
	}

	public ObservableList<String> importFromURLList() {
		ObservableList<String> favorites = null;
		File selectedFile = chooseFile();

		if (selectedFile != null) {
			favorites = importFromURLList(selectedFile);
		}

		return favorites;
	}
	
	protected ObservableList<String> importFromURLList(File file) {
		ObservableList<String> favorites = null;
		
		try {
			FileInputStream fis = new FileInputStream(file);
			LineNumberReader lnr = new LineNumberReader(new InputStreamReader(fis));
			String prevLine = "";
			String line;
			
			while ((line = lnr.readLine()) != null) {
				if (!prevLine.equals(line)) {
					if (isValidURL(line)) {
						if (favorites == null) {
							favorites = FXCollections.observableArrayList();
						}
						
						favorites.addAll(line);
						
						System.out.println("FavoritesImporter.importFromURLList(): line = \"" + line + "\"");
					}
					
					prevLine = line;
				}
			}
			
			lnr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return favorites;
	}
	
	protected boolean isValidURL(String urlString) {
		boolean result = false;
		if (urlString.length() > 0) {
			try {
				URL url = new URL(urlString);
				
				URLConnection conn = url.openConnection();
				
				if (conn instanceof HttpURLConnection) {
					HttpURLConnection httpConn = (HttpURLConnection) conn;
					int resCode = httpConn.getResponseCode();
					
					if (resCode == 200) { // HTTP OK
						result = true;
					}
				}
			} catch (MalformedURLException e) {
				System.out.println("MUE " + urlString);
//				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("IOE " + urlString);
//				e.printStackTrace();
			}
		}
		
		return result;
	}

}
