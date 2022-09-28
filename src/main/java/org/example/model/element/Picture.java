package org.example.model.element;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.App;
import org.example.model.Point;

import java.io.File;
import java.net.MalformedURLException;
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

        pane.getChildren().add(imageView);
    }
}
