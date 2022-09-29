open module javafx.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires xstream;
    requires static lombok;

    exports org.example.controller to javafx.fxml;
}