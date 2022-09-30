package org.example.network;

import lombok.Getter;
import org.example.controller.MainController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Server extends Thread {

    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());
    private static final int PORT = 8080;

    private ServerSocket serverSocket;
    private boolean isRunning;
    private int id = 0;

    public Server() {
        setDaemon(true);
    }

    @Override
    public void run() {
        isRunning = true;
        try {
            serverSocket = new ServerSocket(PORT);
            LOGGER.info("Server started");
            while (isRunning) {
                Socket socket = serverSocket.accept();
                LOGGER.info("New connection");
                try {
                    new Connection(id++, socket);
                } catch (IOException e) {
                    LOGGER.info("Can't create connection");
                }
            }
        } catch (IOException e) {
            isRunning = false;
            LOGGER.info("Can't start server");
        }
    }

    public void shutdown() {
        try {
            isRunning = false;
            serverSocket.close();
        } catch (Exception e) {
            LOGGER.info("Can't close ServerSocket");
        }
    }

}
