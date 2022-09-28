package org.example.controller;

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
import org.example.status.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class MainController {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private final List<AbstractElement> elements = new ArrayList<>();

    private Element element;
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
    }

    public void setElement(Element element) {
        selectedLabel.setText(element.name());
        this.element = element;

        switch (element) {
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
            case DELETE -> {
                infoLabel.setText("");
            }
        }
    }

    public void onWorkspaceClick(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        if (element == null) return;
        switch (element) {
            case TEXT -> {
                Text text = new Text(new Point(x, y), width, height, color, infoLabel.getText());
                text.draw(workspace);
                elements.add(text);
            }
            case PICTURE -> {
                if (file != null) {
                    Picture picture = new Picture(new Point(x, y), width, height, color, file);
                    picture.draw(workspace);
                    elements.add(picture);
                }
            }
            case DELETE -> {
                elements.forEach(element -> {
                    if (element.checkAffiliation(new Point(x, y))) {
                        workspace.getChildren().remove(element.getNode());
                    }
                });
            }
        }

        LOGGER.info("Click " + element + ": x = " + x + " y = " + y);
    }

    public void setWidth(double width) {
        this.width = width;
        widthLabel.setText(Double.toString(width));
    }

    public void setHeight(double height) {
        this.height = height;
        heightLabel.setText(Double.toString(height));
    }
}
