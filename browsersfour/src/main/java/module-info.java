module alantcote.browsersfour {
	requires transitive clutilities;
    requires javafx.fxml;
	requires javafx.web;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.base;
	requires java.prefs;
	
    opens io.github.alantcote.browsersfour to javafx.fxml;
    
    exports io.github.alantcote.browsersfour;
    exports io.github.alantcote.browsersfour.settings;
}