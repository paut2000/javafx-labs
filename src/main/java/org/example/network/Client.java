package org.example.network;

import org.example.model.element.AbstractElement;
import org.example.storing.Serializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {

    private PrintWriter writer;
    private Scanner scanner;
    private Socket socket;

    private Serializer serializer = new Serializer();

    public int requestListSize() {
        open();

        writer.println(Command.REQUEST_LIST_SIZE);
        writer.flush();
        waiting();
        int size = Integer.parseInt(scanner.next());

        close();

        return size;
    }

    public AbstractElement requestObject(int id) {
        open();

        writer.println(Command.REQUEST_OBJECT);
        writer.println(id);
        writer.flush();
        waiting();
        AbstractElement object = serializer.deserializeElementFromXml(scanner.nextLine());

        close();

        return object;
    }

    public List<AbstractElement> requestAllObjects() {
        open();

        writer.println(Command.REQUEST_ALL_OBJECTS);
        writer.flush();
        waiting();
        List<AbstractElement> objects = serializer.deserializeListFromXmlString(scanner.nextLine());

        close();

        return objects;
    }

    public void sendObject(AbstractElement element) {
        open();

        writer.println(Command.SEND_OBJECT);
        writer.println(serializer.serializeElementToXml(element));
        writer.flush();

        close();
    }

    private void open() {
        try {
            socket = new Socket("127.0.0.1", 8080);
            writer = new PrintWriter(socket.getOutputStream());
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void close() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.close();
        scanner.close();
    }

    private void waiting() {
        while (!scanner.hasNext()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
