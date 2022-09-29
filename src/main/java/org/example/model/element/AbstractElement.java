package org.example.model.element;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.Point;
import org.example.storing.Storable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;

@Getter
@Setter
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
public abstract class AbstractElement implements Storable, Serializable {

    protected static final String SEPARATOR = ",";

    protected Point position;
    protected double widthX, heightY;

    protected transient Color color;

    @JsonIgnore
    protected transient boolean isRunning = false;

    @JsonIgnore
    protected transient Transition transition;

    @JsonIgnore
    protected transient Node node;

    public AbstractElement(Point position, double widthX, double heightY, Color color) {
        this.position = position;
        this.widthX = widthX;
        this.heightY = heightY;
        this.color = color;
    }

    public abstract void draw(Pane pane);

    @Override
    public String serialize() { // Текстовая сериализация
        return position.getX() + SEPARATOR + position.getY() + SEPARATOR
                + widthX + SEPARATOR + heightY + SEPARATOR
                + color.toString();
    }

    @Override
    public void deserialize(Scanner scanner) { // Текстовая десериализация
        position = new Point(Double.parseDouble(scanner.next()), Double.parseDouble(scanner.next()));
        widthX = Double.parseDouble(scanner.next());
        heightY = Double.parseDouble(scanner.next());
        color = Color.valueOf(scanner.next());
    }

    // Для бинарной сериализации/десериализации
    // так как класс Color находится в пакете JavaFX и его нельзя изменить
    // а чтобы применить бинарную сериализацию, нужно чтобы класс реализовывал интерфейс-метку Serializable
    public void serializeColor(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeDouble(color.getRed());
        objectOutputStream.writeDouble(color.getGreen());
        objectOutputStream.writeDouble(color.getBlue());
        objectOutputStream.writeDouble(color.getOpacity());
    }

    public void deserializeColor(ObjectInputStream objectInputStream) throws IOException {
        color = new Color(
                objectInputStream.readDouble(),
                objectInputStream.readDouble(),
                objectInputStream.readDouble(),
                objectInputStream.readDouble()
        );
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
