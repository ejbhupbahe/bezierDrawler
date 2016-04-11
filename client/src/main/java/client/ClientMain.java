package client;

import ui.ClientWindow;

import javax.swing.*;

/**
 *  Класс для запуска клиентского приложения
 */
public class ClientMain {
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable(){
            public void run()
            {
                new ClientWindow();
            }
        });
    }
}
