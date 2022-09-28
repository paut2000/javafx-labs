package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCombination;
import lombok.Setter;
import org.example.App;
import org.example.status.Element;

import java.util.Optional;
import java.util.logging.Logger;

public class MenuController {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    @Setter
    private MainController mainController;
    @FXML private ColorPicker colorPicker;
    @FXML private MenuItem widthMenuItem;
    @FXML private MenuItem heightMenuItem;
    @FXML private MenuItem textMenuItem;
    @FXML private MenuItem pictureMenuItem;
    @FXML private MenuItem deleteMenuItem;

    @FXML
    void initialize() {
        textMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        textMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Element/Text\" is selected");
            mainController.setElement(Element.TEXT);
        });

        pictureMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        pictureMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Element/Picture\" is selected");
            mainController.setElement(Element.PICTURE);
        });

        deleteMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        deleteMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Element/Delete\" is selected");
            mainController.setElement(Element.DELETE);
        });

        widthMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        widthMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Property/Width\" is selected");
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Задание ширины");
            dialog.setHeaderText("Введите ширину");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                double width = Double.parseDouble(s);
                mainController.setWidth(width);
            });
        });

        heightMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+H"));
        heightMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Property/Height\" is selected");
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Задание высоты");
            dialog.setHeaderText("Введите высоту");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                double height = Double.parseDouble(s);
                mainController.setHeight(height);
            });
        });

        colorPicker.setOnAction(event -> {
            mainController.setColor(colorPicker.getValue());
        });
    }

}
