open module javafx.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.annotation;
    requires static lombok;
    requires com.fasterxml.jackson.databind;

    exports org.example.controller to javafx.fxml;
}