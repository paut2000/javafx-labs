package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class App extends Application {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public static void main( String[] args ) {
        LOGGER.info("Start application");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("Start JavaFX");

        Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Сетевые протоколы. Данил Калашников.");
        stage.show();
    }
}
