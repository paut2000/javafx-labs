package org.example.model.element;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.*;
import org.example.model.Point;

@Getter
@Setter
public abstract class AbstractElement {

    protected Point position;
    protected double widthX, heightY;
    protected Color color;

    protected boolean isRunning = false;
    protected Transition transition;
    protected Node node;

    public AbstractElement(Point position, double widthX, double heightY, Color color) {
        this.position = position;
        this.widthX = widthX;
        this.heightY = heightY;
        this.color = color;
    }

    public abstract void draw(Pane pane);

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
