package org.example.network.udp;

import org.example.model.element.AbstractElement;
import org.example.storing.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientUdp {

    private static final Logger LOGGER = Logger.getLogger(ClientUdp.class.getName());
    private static final int PORT_IN = 7002;
    private static final int PORT_OUT = 7001;
    private static final InetAddress HOST;

    static {
        try {
            HOST = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private DatagramSocket inputSocket, outputSocket;
    private ByteArrayOutputStream outputStream;
    private DatagramPacket inputPacket;
    private Serializer serializer = new Serializer();

    public void start() {
        try {
            inputSocket = new DatagramSocket(PORT_IN);
            outputSocket = new DatagramSocket();
            byte[] buffer = new byte[65536];
            inputPacket = new DatagramPacket(buffer, buffer.length);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public int requestListSize() {
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(CommandUdp.REQUEST_LIST_SIZE.name().getBytes());
            outputStream.write('\n');
            outputSocket.send(new DatagramPacket(
                    outputStream.toByteArray(),
                    outputStream.size(),
                    HOST, PORT_OUT
            ));
            inputSocket.receive(inputPacket);
            Scanner scanner = new Scanner(
                    new ByteArrayInputStream(inputPacket.getData(), 0, inputPacket.getLength()));
            return Integer.parseInt(scanner.nextLine());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AbstractElement requestObject(int id) {
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(CommandUdp.REQUEST_OBJECT.name().getBytes());
            outputStream.write('\n');
            outputStream.write(Integer.toString(id).getBytes());
            outputStream.write('\n');
            outputSocket.send(new DatagramPacket(
                    outputStream.toByteArray(),
                    outputStream.size(),
                    HOST, PORT_OUT
            ));
            inputSocket.receive(inputPacket);
            Scanner scanner = new Scanner(
                    new ByteArrayInputStream(inputPacket.getData(), 0, inputPacket.getLength()));

            AbstractElement object = serializer.deserializeElementFromXml(scanner.nextLine());

            return object;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AbstractElement> requestAllObjects() {
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(CommandUdp.REQUEST_ALL_OBJECTS.name().getBytes());
            outputStream.write('\n');
            outputSocket.send(new DatagramPacket(
                    outputStream.toByteArray(),
                    outputStream.size(),
                    HOST, PORT_OUT
            ));
            inputSocket.receive(inputPacket);
            Scanner scanner = new Scanner(
                    new ByteArrayInputStream(inputPacket.getData(), 0, inputPacket.getLength()));

            return serializer.deserializeListFromXmlString(scanner.nextLine());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendObject(AbstractElement element) {
        try {
            outputStream = new ByteArrayOutputStream();
            outputStream.write(CommandUdp.SEND_MY_OBJECT.name().getBytes());
            outputStream.write('\n');
            outputStream.write(serializer.serializeElementToXml(element).getBytes());

            outputSocket.send(new DatagramPacket(
                    outputStream.toByteArray(),
                    outputStream.size(),
                    HOST, PORT_OUT
            ));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        inputSocket.close();
        outputSocket.close();
    }

}
