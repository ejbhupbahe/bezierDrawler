package server;

import org.apache.log4j.Logger;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Поток осуществляющий чтение из файла и рассылку данных клиентам.
 */
class EchoThread extends Thread {
    private final static Logger logger = Logger.getLogger(ServerMain.class);
    private Socket socket;
    private static final String FILE_PATH = "serverCommands.txt";

    protected EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        File file = new File(FILE_PATH);
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader data = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));) {
            String line;
            while (true) {
                line = data.readLine();
                if ((line == null)) {
                    break;
                } else {
                    out.println(line);
                    out.flush();
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("Commands file not found: ", e);
            Utils.preventDdos();
        } catch (IOException e) {
            logger.error("Exception occurred: ", e);
        } finally {
            Utils.closeQuietly(socket);
        }
    }


}
