package org.example.model.element;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.Point;

@Getter
@Setter
@NoArgsConstructor
public class Text extends AbstractElement {

    private String text;

    public Text(Point center, double widthX, double heightY, Color color, String text) {
        super(center, widthX, heightY, color);
        this.text = text;
    }

    @Override
    public void draw(Pane pane) {
        Label label = new Label(text);
        label.setLayoutX(super.position.getX());
        label.setLayoutY(super.position.getY());
        label.setTextFill(color);
        node = label;
        pane.getChildren().add(label);
    }
}
