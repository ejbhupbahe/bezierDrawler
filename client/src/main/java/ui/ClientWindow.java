package ui;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
/**
 * Отрисовка окна клиентского приложения
 *
 */
public class ClientWindow extends JPanel {
    public ClientWindow() {
        JPanel cB = new JPanel();
        JFrame frame = new JFrame("Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        Box leftBox = Box.createVerticalBox();
        JPanel tools = new JPanel();
        JPanel up = new JPanel();
        final DrawingPanel drawing = new DrawingPanel();
        JScrollPane scrollDrawing = new JScrollPane(drawing);
        scrollDrawing.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDrawing.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        final JButton connect = new JButton(" Connect     ");
        JButton disconnect = new JButton("Disconnect");
        JButton clear = new JButton("     Clear       ");
        clear.setBackground(Color.WHITE);
        connect.setBackground(Color.WHITE);
        disconnect.setBackground(Color.WHITE);

        leftBox.add(connect);
        leftBox.add(Box.createVerticalStrut(5));
        leftBox.add(disconnect);
        leftBox.add(Box.createVerticalStrut(5));
        leftBox.add(clear);
        leftBox.add(Box.createVerticalStrut(5));
        leftBox.add(cB);
        leftBox.add(Box.createVerticalStrut(5));
        leftBox.add(Box.createVerticalGlue());
        tools.add(leftBox);
        frame.add(tools, BorderLayout.WEST);
        frame.add(up, BorderLayout.EAST);
        frame.add(scrollDrawing, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(800, 700);



        frame.addComponentListener(new  ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                drawing.resizeField();
            }
        });



        clear.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent qw)
            {
                drawing.clearPanel();
            }
        });
        connect.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent qw) {
                   drawing.connectData();
                connect.setEnabled(false);

            }
        });

        disconnect.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent qw) {
                drawing.disconnectData();
                drawing.clearPanel();
                connect.setEnabled(true);

            }
        });

    }

}

