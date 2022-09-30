package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import lombok.Setter;
import org.example.App;
import org.example.model.element.AbstractElement;
import org.example.network.Server;
import org.example.status.Singleton;
import org.example.status.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MenuController {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    @Setter private MainController mainController;
    private Server server;


    @FXML private ColorPicker colorPicker;
    @FXML private MenuItem widthMenuItem;
    @FXML private MenuItem heightMenuItem;
    @FXML private MenuItem textMenuItem;
    @FXML private MenuItem pictureMenuItem;
    @FXML private MenuItem deleteMenuItem;
    @FXML public MenuItem startAllMenuItem;
    @FXML public MenuItem stopAllMenuItem;
    @FXML public MenuItem startOneMenuItem;
    @FXML public MenuItem stopOneMenuItem;
    @FXML public MenuItem serializeTextMenuItem;
    @FXML public MenuItem serializeBinaryMenuItem;
    @FXML public MenuItem serializeXmlMenuItem;
    @FXML public MenuItem serializeAllMenuItem;
    @FXML public MenuItem deserializeTextMenuItem;
    @FXML public MenuItem deserializeBinaryMenuItem;
    @FXML public MenuItem deserializeXmlMenuItem;
    @FXML private CheckMenuItem serverCheckMenuItem;
    @FXML public MenuItem selectStrangerMenuItem;
    @FXML public MenuItem selectAllStrangerMenuItem;
    @FXML public MenuItem selectMyMenuItem;

    @FXML
    void initialize() {
        textMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+T"));
        textMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Element/Text\" is selected");
            mainController.setClickType(ClickType.TEXT);
        });

        pictureMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        pictureMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Element/Picture\" is selected");
            mainController.setClickType(ClickType.PICTURE);
        });

        deleteMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+D"));
        deleteMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Element/Delete\" is selected");
            mainController.setClickType(ClickType.DELETE);
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
            LOGGER.info("Menu item \"Property/Color\" is selected");
            mainController.setColor(colorPicker.getValue());
        });

        startAllMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Property/Start All\" is selected");
            mainController.startMovementAll();
        });

        stopAllMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Property/Stop All\" is selected");
            mainController.stopMovementAll();
        });

        startOneMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Property/Start One\" is selected");
            mainController.setClickType(ClickType.STAR_MOVEMENT);
        });

        stopOneMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Property/Stop One\" is selected");
            mainController.setClickType(ClickType.STOP_MOVEMENT);
        });

        serializeXmlMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Serialization/XML\" is selected");
            mainController.serializeToXml();
        });

        deserializeXmlMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Deserialization/XML\" is selected");
            mainController.deserializeFromXml();
        });

        serializeBinaryMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Serialization/Binary\" is selected");
            mainController.serializeToBinary();
        });

        deserializeBinaryMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Deserialization/Binary\" is selected");
            mainController.deserializeFromBinary();
        });

        serializeTextMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Serialization/Text\" is selected");
            mainController.serializeToText();
        });

        deserializeTextMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Deserialization/Text\" is selected");
            mainController.deserializeFromText();
        });

        serializeAllMenuItem.setOnAction(event -> {
            LOGGER.info("Menu item \"Serialization/All\" is selected");
            mainController.serializeToAllFormats();
        });

        serverCheckMenuItem.setOnAction(event -> {
            if (serverCheckMenuItem.isSelected()) {
                LOGGER.info("Menu item \"Network/Server\" is selected");
                server = new Server();
                server.start();
            } else {
                LOGGER.info("Menu item \"Network/Server\" is unselected");
                server.shutdown();
                server = null;
                selectStrangerMenuItem.setDisable(true);
            }
        });

        selectStrangerMenuItem.setOnAction(event -> {
            int size = Singleton.getInstance().getClient().requestListSize();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(String.valueOf(i));
            }
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Объекты", list);
            dialog.setTitle("Подключение");
            dialog.setHeaderText("Выбирите объект");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(s -> {
                AbstractElement element = Singleton.getInstance().getClient().requestObject(Integer.parseInt(s));
                mainController.addElement(element);
            });
        });

        selectAllStrangerMenuItem.setOnAction(event -> {
            mainController.addElements(Singleton.getInstance().getClient().requestAllObjects());
        });

        selectMyMenuItem.setOnAction(event -> {
            mainController.setClickType(ClickType.SEND_OBJECT);
        });
    }

}
