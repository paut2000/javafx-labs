package org.example.network.udp;

import org.example.model.element.AbstractElement;
import org.example.status.Singleton;
import org.example.storing.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.logging.Logger;

public class ServerUdp extends Thread {

    private static final Logger LOGGER = Logger.getLogger(ServerUdp.class.getName());
    private static final int PORT_IN = 7001;
    private static final int PORT_OUT = 7002;

    private DatagramSocket inputSocket, outputSocket;
    private boolean isRunning;
    private final Serializer serializer = new Serializer();

    public ServerUdp() {
        setDaemon(true);
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            inputSocket = new DatagramSocket(PORT_IN);
            outputSocket = new DatagramSocket();
            byte[] buffer = new byte[65536];
            DatagramPacket inputPacket = new DatagramPacket(buffer, buffer.length);
            LOGGER.info("Server started");
            while (isRunning) {
                inputSocket.receive(inputPacket);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(inputPacket.getData(), 0, inputPacket.getLength());
                Scanner scanner = new Scanner(inputStream);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                switch (CommandUdp.valueOf(scanner.nextLine())) {
                    case REQUEST_LIST_SIZE -> {
                        outputStream.write(
                                Integer.toString(
                                    Singleton.getInstance().getElements().size()
                                ).getBytes()
                        );
                    }
                    case REQUEST_OBJECT -> {
                        int id = scanner.nextInt();
                        outputStream.write(
                                serializer.serializeElementToXml(
                                        Singleton.getInstance().getElements().get(id)
                                ).getBytes());
                    }
                    case REQUEST_ALL_OBJECTS -> {
                        outputStream.write(
                                serializer.serializeListToXmlString(
                                        Singleton.getInstance().getElements()
                                ).getBytes());
                    }
                    case SEND_MY_OBJECT -> {
                        AbstractElement element = serializer.deserializeElementFromXml(scanner.nextLine());
                        Singleton.getInstance().getOneElementProperty().set(element);
                    }
                }

                outputSocket.send(new DatagramPacket(
                        outputStream.toByteArray(),
                        outputStream.size(),
                        InetAddress.getByName("127.0.0.1"),
                        PORT_OUT
                ));

            }
        } catch (IOException e) {
            isRunning = false;
            LOGGER.info("Can't start server");
        }
    }

    public void shutdown() {
        try {
            isRunning = false;
            inputSocket.close();
            outputSocket.close();
        } catch (Exception e) {
            LOGGER.info("Can't close ServerSocket");
        }
    }


}
