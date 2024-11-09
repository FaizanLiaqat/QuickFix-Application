module QuickFixv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
	requires java.sql;

    opens application to javafx.graphics, javafx.fxml;  // For application package
    opens home to javafx.fxml;                          // For home package, needed for FXML loading
    opens quickFixAllClasses to javafx.fxml;                 // For moduleClasses package, needed if it contains controllers
    opens register to javafx.fxml;                 // For moduleClasses package, needed if it contains controllers
    
    exports application;                                // Export application package if needed outside
    exports quickFixAllClasses;                              // Export moduleClasses if needed outside
    exports register;
}
