package org.example.model.element;

import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.App;
import org.example.model.Point;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.logging.Logger;

@Getter
@Setter
@NoArgsConstructor
public class Picture extends AbstractElement {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private File file;

    public Picture(Point center, double widthX, double heightY, Color color, File file) {
        super(center, widthX, heightY, color);
        this.file = file;
    }

    @Override
    public void draw(Pane pane) {
        ImageView imageView = null;
        try {
            imageView = new ImageView(new Image(file.toURI().toURL().toExternalForm()));
        } catch (MalformedURLException e) {
            LOGGER.info("Bad URL");
            return;
        }

        imageView.setFitWidth(widthX);
        imageView.setFitHeight(heightY);
        imageView.setX(position.getX());
        imageView.setY(position.getY());
        node = imageView;
        transition = createScaleTransition();

        pane.getChildren().add(imageView);
    }

    @Override
    public String serialize() {
        return getClass().getName() + SEPARATOR + file.toString() + SEPARATOR + super.serialize() + ";";
    }

    @Override
    public void deserialize(Scanner scanner) {
        file = new File(scanner.next());
        super.deserialize(scanner);
    }


    private Transition createScaleTransition() {
        ScaleTransition translate = new ScaleTransition();
        translate.setToX(0.5);
        translate.setToY(0.5);
        translate.setFromX(1);
        translate.setFromY(1);
        translate.setDuration(Duration.millis(1000));
        translate.setCycleCount(-1);
        translate.setAutoReverse(true);
        translate.setNode(node);
        return translate;
    }
}
