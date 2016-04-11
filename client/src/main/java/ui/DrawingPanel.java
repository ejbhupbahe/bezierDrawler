package ui;

import client.Client;
import dto.ConnectionCommandsDto;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Панель отображения кривых
 */

public class DrawingPanel extends JPanel implements Runnable {
    private final static Logger logger = Logger.getLogger(DrawingPanel.class);
    private BufferedImage img, state;
    private BezierDrawing bezierDrawing = new BezierDrawing();
    private Client client;
    private ExecutorService threadPool = null;


    public DrawingPanel() {
        this.setOpaque(true);
        this.setBorder(BorderFactory.createLoweredBevelBorder());
        this.setBackground(Color.WHITE);
        client = new Client();
    }

    @Override
    public void run() {
        BlockingQueue<ConnectionCommandsDto> commands = client.getCommands();
        List<List<Point>> pts = new ArrayList<>();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ConnectionCommandsDto command = commands.take();
                logger.info("Processing command: " + command);

                if (command.equals(Client.CLEAR_COMMAND)) {
                    this.clearPanel();
                    continue;
                }

                if (command.getCommand().equals("start")) {
                    drawCurve(pts, command.getColor());
                    pts.add(new ArrayList<Point>());
                }

                if (pts.size() == 0) {
                    continue;
                }
                Point e = calculateRelativePoint(command);
                pts.get(pts.size() - 1).add(e);
                this.repaint();
            } catch (InterruptedException e) {
                logger.warn("Interrupted", e);
                throw new RuntimeException(e);
            } catch (RuntimeException e) {
                logger.error("", e);
            }
        }
    }

    private void savePreviousImg() {
        state = new BufferedImage(DrawingPanel.this.getWidth(), DrawingPanel.this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = state.getGraphics();
        g.drawImage(img, 0, 0, null);
    }


    private void loadPreviousState() {
        Graphics g = img.getGraphics();
        g.drawImage(state, 0, 0, DrawingPanel.this);
    }

    public void resizeField() {
        BufferedImage copy = this.img;
        this.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D d2 = this.img.createGraphics();
        d2.setColor(Color.white);
        d2.fillRect(0, 0, this.getWidth(), this.getHeight());
        d2.drawImage(copy, null, 0, 0);
        this.loadPreviousState();
        this.savePreviousImg();
    }


    protected void paintComponent(Graphics g) {
        if (img == null) {
            img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D d2 = img.createGraphics();
            d2.setColor(Color.white);
            d2.fillRect(0, 0, DrawingPanel.this.getWidth(), this.getHeight());
        }
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this);
    }

    public void clearPanel() {
        img = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        g.setColor(this.getBackground());
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        this.repaint();
        this.savePreviousImg();
    }


    public void connectData() {
        logger.info("Connecting to server...");
        this.savePreviousImg();
        threadPool = Executors.newFixedThreadPool(2);
        threadPool.submit(client);
        threadPool.submit(this);
    }

    public void disconnectData() {
        if (threadPool != null) {
            threadPool.shutdownNow();
            try {
                threadPool.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Point calculateRelativePoint(ConnectionCommandsDto command) {
        Point e = new Point();
        e.x = (int) (this.img.getWidth() * (Double.parseDouble(command.getPointX())));
        e.y = (int) (this.img.getHeight() * (Double.parseDouble(command.getPointY())));
        return e;
    }

    private void drawCurve(List<List<Point>> pts, Color color) {
        for (List<Point> pt : pts) {
            Graphics g = this.img.getGraphics();
            g.setColor(color);
            bezierDrawing.bezierDrawing(pt, g);
            pt.clear();
        }
    }
}
