package org.example.status;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import org.example.model.element.AbstractElement;
import org.example.network.Client;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private static Singleton instance;

    @Getter private List<AbstractElement> elements = new ArrayList<>();
    @Getter private Client client = new Client();
    @Getter private StringProperty stringProperty = new SimpleStringProperty();

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private Singleton() {}

}
