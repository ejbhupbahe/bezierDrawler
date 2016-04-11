package server;

import org.apache.log4j.Logger;
import utils.Utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Класс для запуска серверного приложения для одновременной работы с несколькими клиентами
 */

public class ServerMain {
    private final static Logger logger = Logger.getLogger(ServerMain.class);

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(Utils.PORT);
        } catch (IOException e) {
            logger.error("ServerSocket IOException: " + e);

        }
        while (!Thread.currentThread().isInterrupted()) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                logger.error("I/O error: " + e);
            }
            new EchoThread(socket).start();
        }
    }
}

