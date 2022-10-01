package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import lombok.Setter;
import org.example.App;
import org.example.model.element.AbstractElement;
import org.example.network.tcp.ServerTcp;
import org.example.network.udp.ClientUdp;
import org.example.network.udp.ServerUdp;
import org.example.status.Singleton;
import org.example.status.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MenuController {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    @Setter private MainController mainController;
    private ServerTcp serverTcp;
    private ServerUdp serverUdp;

    @FXML private ColorPicker colorPicker;
    @FXML private MenuItem widthMenuItem;
    @FXML private MenuItem heightMenuItem;
    @FXML private MenuItem textMenuItem;
    @FXML private MenuItem pictureMenuItem;
    @FXML private MenuItem deleteMenuItem;
    @FXML private MenuItem startAllMenuItem;
    @FXML private MenuItem stopAllMenuItem;
    @FXML private MenuItem startOneMenuItem;
    @FXML private MenuItem stopOneMenuItem;
    @FXML private MenuItem serializeTextMenuItem;
    @FXML private MenuItem serializeBinaryMenuItem;
    @FXML private MenuItem serializeXmlMenuItem;
    @FXML private MenuItem serializeAllMenuItem;
    @FXML private MenuItem deserializeTextMenuItem;
    @FXML private MenuItem deserializeBinaryMenuItem;
    @FXML private MenuItem deserializeXmlMenuItem;
    @FXML private CheckMenuItem serverCheckMenuItem;
    @FXML private MenuItem selectStrangerMenuItem;
    @FXML private MenuItem selectAllStrangerMenuItem;
    @FXML private MenuItem selectMyMenuItem;
    @FXML private CheckMenuItem serverUdpCheckMenuItem;
    @FXML private CheckMenuItem clientUdpCheckMenuItem;
    @FXML private MenuItem selectStrangerUdpMenuItem;
    @FXML private MenuItem selectMyUdpMenuItem;
    @FXML private MenuItem selectAllStrangerUdpMenuItem;

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
                serverTcp = new ServerTcp();
                serverTcp.start();

                selectStrangerMenuItem.setDisable(true);
                selectAllStrangerMenuItem.setDisable(true);
                selectMyMenuItem.setDisable(true);
            } else {
                LOGGER.info("Menu item \"Network/Server\" is unselected");
                serverTcp.shutdown();
                serverTcp = null;

                selectStrangerMenuItem.setDisable(false);
                selectAllStrangerMenuItem.setDisable(false);
                selectMyMenuItem.setDisable(false);
            }
        });

        selectStrangerMenuItem.setOnAction(event -> {
            int size = Singleton.getInstance().getClientTcp().requestListSize();
            selectObjectDialog(size).ifPresent(s -> {
                AbstractElement element = Singleton.getInstance().getClientTcp().requestObject(Integer.parseInt(s));
                mainController.addElement(element);
            });
        });

        selectAllStrangerMenuItem.setOnAction(event -> {
            mainController.addElements(Singleton.getInstance().getClientTcp().requestAllObjects());
        });

        selectMyMenuItem.setOnAction(event -> {
            mainController.setClickType(ClickType.SEND_OBJECT_TCP);
        });

        serverUdpCheckMenuItem.setOnAction(actionEvent -> {
            if (serverUdpCheckMenuItem.isSelected()) {
                serverUdp = new ServerUdp();
                serverUdp.start();

                clientUdpCheckMenuItem.setDisable(true);

            } else {
                serverUdp.shutdown();
                serverUdp = null;

                clientUdpCheckMenuItem.setDisable(false);
            }
        });

        clientUdpCheckMenuItem.setOnAction(actionEvent -> {
            if (clientUdpCheckMenuItem.isSelected()) {
                Singleton.getInstance().getClientUdp().start();

                serverUdpCheckMenuItem.setDisable(true);
                selectStrangerUdpMenuItem.setDisable(false);
                selectMyUdpMenuItem.setDisable(false);
                selectAllStrangerUdpMenuItem.setDisable(false);
            } else {
                Singleton.getInstance().getClientUdp().shutdown();

                serverUdpCheckMenuItem.setDisable(false);
                selectStrangerUdpMenuItem.setDisable(true);
                selectMyUdpMenuItem.setDisable(true);
                selectAllStrangerUdpMenuItem.setDisable(true);
            }
        });

        selectStrangerUdpMenuItem.setOnAction(actionEvent -> {
            int size = Singleton.getInstance().getClientUdp().requestListSize();
            selectObjectDialog(size).ifPresent(s -> {
                AbstractElement element = Singleton.getInstance().getClientUdp().requestObject(Integer.parseInt(s));
                mainController.addElement(element);
            });
        });

        selectMyUdpMenuItem.setOnAction(actionEvent -> {
            mainController.setClickType(ClickType.SEND_OBJECT_UDP);
        });

        selectAllStrangerUdpMenuItem.setOnAction(actionEvent -> {
            mainController.addElements(Singleton.getInstance().getClientUdp().requestAllObjects());
        });
    }

    private Optional<String> selectObjectDialog(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(String.valueOf(i));
        }
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Объекты", list);
        dialog.setTitle("Подключение");
        dialog.setHeaderText("Выбирите объект");
        return dialog.showAndWait();
    }

}
