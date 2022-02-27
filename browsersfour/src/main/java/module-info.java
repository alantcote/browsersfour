module alantcote.browsersfour {
	requires transitive fxutilities;
    requires javafx.fxml;
	requires javafx.web;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires java.prefs;
	requires java.xml;
	requires java.base;
	requires jdk.xml.dom;
	
    opens io.github.alantcote.browsersfour to javafx.fxml;
    
    exports io.github.alantcote.browsersfour;
    exports io.github.alantcote.browsersfour.settings;
}