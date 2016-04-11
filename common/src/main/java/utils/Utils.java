package utils;

import java.io.IOException;
import java.net.Socket;

/**
 * Класс с небольшими утилитными методами.
 */
public final class Utils {
    public static final int PORT = 29288;
    private Utils() {
    }

    public static void preventDdos() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {
        }
    }

    public static void closeQuietly(Socket socket) {
        try {
            if(socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

}
