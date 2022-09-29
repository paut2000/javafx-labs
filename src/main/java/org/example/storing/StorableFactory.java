package org.example.storing;

import org.example.App;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;
import java.util.logging.Logger;

public class StorableFactory {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    public Storable create(Scanner scanner) {
        Storable object = null;
        String className = scanner.next();
        try {
            object = (Storable) Class.forName(className).getDeclaredConstructor().newInstance();
            object.deserialize(scanner);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 ClassNotFoundException e) {
            LOGGER.info("Can't create object");
        }

        return object;
    }

}
