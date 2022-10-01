package org.example.status;

import javafx.beans.property.*;
import lombok.Getter;
import org.example.model.element.AbstractElement;
import org.example.network.tcp.ClientTcp;
import org.example.network.udp.ClientUdp;

import java.util.ArrayList;
import java.util.List;

public class Singleton {

    private static Singleton instance;

    @Getter private final List<AbstractElement> elements = new ArrayList<>();
    @Getter private final ClientTcp clientTcp = new ClientTcp();
    @Getter private final ClientUdp clientUdp = new ClientUdp();
    @Getter private final ObjectProperty<AbstractElement> oneElementProperty = new SimpleObjectProperty<>();

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    private Singleton() {}

}
