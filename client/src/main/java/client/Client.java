package client;

import dto.ConnectionCommandsDto;
import org.apache.log4j.Logger;
import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
/**
 * Класс отвечающий за подключение клиента к серверу
 *
 */

public class Client implements Runnable {
    private final static Logger logger = Logger.getLogger(Client.class);

    private BlockingQueue<ConnectionCommandsDto> commands = new ArrayBlockingQueue<>(10);
    public static final ConnectionCommandsDto CLEAR_COMMAND = new ConnectionCommandsDto();

    public Client() {
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            String line;
            try (Socket skt = new Socket("localhost", Utils.PORT)) {
                commands.put(CLEAR_COMMAND);
                BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
                while ((line = in.readLine()) != null) {
                    ConnectionCommandsDto comm = new ConnectionCommandsDto(line);
                    logger.info("Accepted command: " + line);
                    commands.put(comm);
                }
            } catch (InterruptedException e) {
                logger.warn("Interrupted: ", e);
            } catch (RuntimeException|IOException ignored) {
                Utils.preventDdos();
                logger.error("Exception occurred: ", ignored);
            }
        }
    }

    public BlockingQueue<ConnectionCommandsDto> getCommands() {
        return commands;
    }


}



