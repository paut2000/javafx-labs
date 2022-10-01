package org.example.network.tcp;

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
        CommandTcp commandTcp = CommandTcp.valueOf(scanner.nextLine());
        
        switch (commandTcp) {
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
                AbstractElement element = serializer.deserializeElementFromXml(scanner.nextLine());
                Singleton.getInstance().getOneElementProperty().set(element);
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
