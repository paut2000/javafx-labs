package org.example.model.element;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.*;
import org.example.model.Point;
import org.example.storing.Storable;

import java.io.Serializable;
import java.util.Scanner;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractElement implements Storable, Serializable {

    protected static final String SEPARATOR = ",";

    protected Point position;
    protected double widthX, heightY;
    protected transient Color color;

    protected transient boolean isRunning = false;
    protected transient Transition transition;
    protected transient Node node;

    public AbstractElement(Point position, double widthX, double heightY, Color color) {
        this.position = position;
        this.widthX = widthX;
        this.heightY = heightY;
        this.color = color;
    }

    public abstract void draw(Pane pane);

    @Override
    public String serialize() {
        return position.getX() + SEPARATOR + position.getY() + SEPARATOR
                + widthX + SEPARATOR + heightY + SEPARATOR
                + color.toString();
    }

    @Override
    public void deserialize(Scanner scanner) {
        position = new Point(Double.parseDouble(scanner.next()), Double.parseDouble(scanner.next()));
        widthX = Double.parseDouble(scanner.next());
        heightY = Double.parseDouble(scanner.next());
        color = Color.valueOf(scanner.next());
    }

    public void startMove() {
        if (isRunning) return;
        isRunning = true;
        transition.play();
    }

    public void stopMove() {
        isRunning = false;
        transition.stop();
    }

    public boolean checkAffiliation(final Point point) {
        final double top = position.getY();
        final double bottom = position.getY() + heightY;
        final double left = position.getX();
        final double right = position.getX() + widthX;

        if (top > point.getY()) return false;
        if (bottom < point.getY()) return false;
        if (left > point.getX()) return false;
        if (right < point.getX()) return false;

        return true;
    }

}
