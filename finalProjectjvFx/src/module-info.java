/**
 * 
 */
/**
 * 
 */
module finalProjectjFx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens controller to javafx.fxml;
    opens view to javafx.fxml;
    opens model to javafx.base;

    exports controller;
    exports model;
    exports dao;
    exports dao.impli;
    exports util;
    exports view;
}
