package dto;


import org.apache.log4j.Logger;
import ui.DrawingPanel;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс преобразования комманд сервера
 */

public class ConnectionCommandsDto {
    private final static Logger logger = Logger.getLogger(DrawingPanel.class);
    private String macadress;
    private String command;
    private String pointX;
    private String pointY;
    private Color color;

    public ConnectionCommandsDto() {
    }

    public ConnectionCommandsDto(String s) {
        if (checkString(s)) {
            logger.info("Server command accepted");
            String[] parts = s.split(";");
            this.macadress = parts[0];
            this.command = parts[1];
            this.pointX = parts[2];
            this.pointY = parts[3];
            this.color = convertColor(parts[4]);
        } else {
            logger.error("Server command ignored: " + s);
        }
    }

    private static boolean checkString(String string) {
//        String mac = "([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])";
//        String comm = "start|move";
//        String point = "[-+]?(?:\\b[0-9]+(?:\\.[0-9]*)?|\\.[0-9]+\\b)(?:[eE][-+]?[0-9]+\\b)?";
        String line = "([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F];)(start;|move;)([-+]?[0-9]*\\.?[0-9]*;)([-+]?[0-9]*\\.?[0-9]*;)([-+]?[0-9]*\\.?[0-9]*)";

        Pattern p = Pattern.compile(line);
        Matcher m = p.matcher(string);
        return m.matches();
    }

    public String getMacadress() {
        return macadress;
    }

    public void setMacadress(String macadress) {
        this.macadress = macadress;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPointX() {
        return pointX;
    }

    public void setPointX(String pointX) {
        this.pointX = pointX;
    }

    public String getPointY() {
        return pointY;
    }

    public void setPointY(String pointY) {
        this.pointY = pointY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ConnectionCommandsDto{" +
                "macadress='" + macadress + '\'' +
                ", command='" + command + '\'' +
                ", pointX='" + pointX + '\'' +
                ", pointY='" + pointY + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public Color convertColor(String color) {
        return new Color(Integer.parseInt(color), true);
    }


}
