module javafx.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires static lombok;

    opens org.example;

    exports org.example.controller to javafx.fxml;
    opens org.example.controller;
}