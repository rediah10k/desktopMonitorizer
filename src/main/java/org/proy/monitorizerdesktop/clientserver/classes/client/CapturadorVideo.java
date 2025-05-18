package org.proy.monitorizerdesktop.clientserver.classes.client;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Component
public class CapturadorVideo {
    Socket socket;
    DataOutputStream dos;

    public void setProperties(String host, Integer puerto){
        try{
            socket = new Socket(host, puerto);
             dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void capturarVideo() {
        try{
        Robot robot = new Robot();
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

        while (true) {
            BufferedImage screen = robot.createScreenCapture(screenRect);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screen, "jpg", baos);
            byte[] imageBytes = baos.toByteArray();

            dos.writeInt(imageBytes.length);
            dos.write(imageBytes);
            dos.flush();
        }}catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
