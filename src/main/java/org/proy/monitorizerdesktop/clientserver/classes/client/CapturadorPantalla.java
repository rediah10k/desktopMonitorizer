package org.proy.monitorizerdesktop.clientserver.classes.client;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CapturadorPantalla {
    private Robot robot;
    private Rectangle screenRect;

    public CapturadorPantalla() {
        try {
            robot = new Robot();
            screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage capturar() {
        return robot.createScreenCapture(screenRect);
    }

    public Dimension getScreenSize() {
        return screenRect.getSize();
    }
}

