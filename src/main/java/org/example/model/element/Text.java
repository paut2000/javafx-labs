package org.example.model.element;

import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.Point;

import java.util.Scanner;

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
        Label label = createCustomLabel();
        node = label;
        transition = createRotateTransition();
        pane.getChildren().add(label);
    }

    @Override
    public String serialize() {
        return getClass().getName() + SEPARATOR + text + SEPARATOR + super.serialize() + ";";
    }

    @Override
    public void deserialize(Scanner scanner) {
        text = scanner.next();
        super.deserialize(scanner);
    }

    private Transition createRotateTransition() {
        RotateTransition rotateTransition = new RotateTransition();
        rotateTransition.setDuration(Duration.millis(1000));
        rotateTransition.setNode(node);
        rotateTransition.setCycleCount(-1);
        rotateTransition.setByAngle(360);
        return rotateTransition;
    }

    private Label createCustomLabel() {
        Label label = new Label(text);
        label.setLayoutX(super.position.getX());
        label.setLayoutY(super.position.getY());
        label.setTextFill(color);
        return label;
    }

}
