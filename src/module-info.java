module QuickFixv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    opens application to javafx.graphics, javafx.fxml;  // For main application package
    opens models to javafx.fxml;                        // For model classes (if needed for FXML)
    opens controllers to javafx.fxml;                   // For controller classes

    exports application;
    exports models;
    exports controllers;                                // Export if needed outside this module
    exports views;                                      // Export if needed outside this module
}
