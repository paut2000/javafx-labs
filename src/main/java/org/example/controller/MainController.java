package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import lombok.Setter;
import org.example.App;
import org.example.model.Point;
import org.example.model.element.AbstractElement;
import org.example.model.element.Picture;
import org.example.model.element.Text;
import org.example.status.Singleton;
import org.example.status.ClickType;
import org.example.storing.Serializer;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MainController {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private final Serializer serializer = new Serializer();

    private ClickType clickType;
    private File file;
    private double width = 100;
    private double height = 100;
    @Setter private Color color = Color.BLACK;

    @FXML private MenuController menuController;
    @FXML private Label selectedLabel;
    @FXML private Label infoLabel;
    @FXML public Label widthLabel;
    @FXML public Label heightLabel;
    @FXML private AnchorPane workspace;

    @FXML
    void initialize() {
        menuController.setMainController(this);
        workspace.setOnMouseClicked(this::onWorkspaceClick);
        heightLabel.setText(Double.toString(height));
        widthLabel.setText(Double.toString(width));
        listenProperty();
    }

    //когда выбираю в меню
    public void setClickType(ClickType clickType) {
        selectedLabel.setText(clickType.name());
        this.clickType = clickType;

        switch (clickType) {
            case TEXT -> {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Создание текста");
                dialog.setHeaderText("Введите текст");
                Optional<String> result = dialog.showAndWait();
                result.ifPresentOrElse(s -> infoLabel.setText(s), () -> infoLabel.setText("Default"));
            }
            case PICTURE -> {
                File file = new FileChooser().showOpenDialog(workspace.getScene().getWindow());
                if (file != null) {
                    infoLabel.setText(file.getAbsolutePath());
                    this.file = file;
                }
            }
            case DELETE, STOP_MOVEMENT, STAR_MOVEMENT -> infoLabel.setText("");
        }
    }

    //когда кликаю мышкой
    public void onWorkspaceClick(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        if (clickType == null) return;
        switch (clickType) {
            case TEXT -> {
                Text text = new Text(new Point(x, y), width, height, color, infoLabel.getText());
                text.draw(workspace);
                Singleton.getInstance().getElements().add(text);
            }
            case PICTURE -> {
                if (file != null) {
                    Picture picture = new Picture(new Point(x, y), width, height, color, file);
                    picture.draw(workspace);
                    Singleton.getInstance().getElements().add(picture);
                }
            }
            case DELETE -> {
                Singleton.getInstance().getElements().removeIf(element -> {
                    if (element.checkAffiliation(new Point(x, y))) {
                        workspace.getChildren().remove(element.getNode());
                        return true;
                    }
                    return false;
                });
            }
            case STAR_MOVEMENT -> {
                Singleton.getInstance().getElements().forEach(element -> {
                    if (element.checkAffiliation(new Point(x, y))) {
                        element.startMove();
                    }
                });
            }
            case STOP_MOVEMENT -> {
                Singleton.getInstance().getElements().forEach(element -> {
                    if (element.checkAffiliation(new Point(x, y))) {
                        element.stopMove();
                    }
                });
            }
            case SEND_OBJECT_TCP -> {
                Singleton.getInstance().getElements().forEach(element -> {
                    if (element.checkAffiliation(new Point(x, y))) {
                        Singleton.getInstance().getClientTcp().sendObject(element);
                    }
                });
            }
            case SEND_OBJECT_UDP -> {
                Singleton.getInstance().getElements().forEach(element -> {
                    if (element.checkAffiliation(new Point(x, y))) {
                        Singleton.getInstance().getClientUdp().sendObject(element);
                    }
                });
            }
        }

        LOGGER.info("Click " + clickType + ": x = " + x + " y = " + y);
    }

    public void listenProperty() {
        Singleton.getInstance().getOneElementProperty().addListener((observableValue, oldValue, newValue) -> {
            Platform.runLater(() -> {
                addElement(newValue);
            });
        });
    }

    public void setWidth(double width) {
        this.width = width;
        widthLabel.setText(Double.toString(width));
    }

    public void setHeight(double height) {
        this.height = height;
        heightLabel.setText(Double.toString(height));
    }

    public void startMovementAll() {
        Singleton.getInstance().getElements().forEach(AbstractElement::startMove);
    }

    public void stopMovementAll() {
        Singleton.getInstance().getElements().forEach(AbstractElement::stopMove);
    }

    public void serializeToXml() {
        serializer.serializeToXmlFile(Singleton.getInstance().getElements());
    }

    public void deserializeFromXml() {
        List<AbstractElement> list = serializer.deserializeFromXmlFile();
        list.forEach(element -> element.draw(workspace));
        Singleton.getInstance().getElements().addAll(list);
    }

    public void serializeToBinary() {
        serializer.serializeToBinaryFile(Singleton.getInstance().getElements());
    }

    public void deserializeFromBinary() {
        List<AbstractElement> list = serializer.deserializeFromBinaryFile();
        list.forEach(element -> element.draw(workspace));
        Singleton.getInstance().getElements().addAll(list);
    }

    public void serializeToText() {
        serializer.serializeToTextFile(Singleton.getInstance().getElements());
    }

    public void deserializeFromText() {
        List<AbstractElement> list = (List<AbstractElement>) serializer.deserializeFromTextFile();
        list.forEach(element -> element.draw(workspace));
        Singleton.getInstance().getElements().addAll(list);
    }

    public void serializeToAllFormats() {
        serializeToText();
        serializeToBinary();
        serializeToXml();
    }

    public void addElement(AbstractElement element) {
        Singleton.getInstance().getElements().add(element);
        element.draw(workspace);
    }

    public void addElements(List<AbstractElement> elements) {
        Singleton.getInstance().getElements().addAll(elements);
        elements.forEach(element -> element.draw(workspace));
    }

}
