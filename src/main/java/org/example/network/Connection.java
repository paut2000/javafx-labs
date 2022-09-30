package org.example.network;

import lombok.Getter;
import lombok.Setter;
import org.example.controller.MainController;
import org.example.model.element.AbstractElement;
import org.example.status.Singleton;
import org.example.storing.Serializer;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class Connection extends Thread {

    private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

    private Socket socket;
    private Scanner scanner;
    private PrintWriter writer;
    private int id;

    private final Serializer serializer = new Serializer();

    public Connection(int id, Socket socket) throws IOException {
        this.socket = socket;
        this.id = id;
        setDaemon(true);
        scanner = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream());
        this.start();
    }

    @Override
    public void run() {
        Command command = Command.valueOf(scanner.nextLine());
        
        switch (command) {
            case REQUEST_LIST_SIZE -> {
                writer.println(Singleton.getInstance().getElements().size());
            }
            case REQUEST_OBJECT -> {
                int id = Integer.parseInt(scanner.nextLine());
                writer.println(serializer.serializeElementToXml(Singleton.getInstance().getElements().get(id)));
            }
            case REQUEST_ALL_OBJECTS -> {
                writer.println(serializer.serializeListToXmlString(Singleton.getInstance().getElements()));
            }
            case SEND_OBJECT -> {
                Singleton.getInstance().getStringProperty().set(scanner.nextLine());
            }
        }

        writer.flush();

        scanner.close();
        writer.close();
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info("Connection is closed");
    }

}
