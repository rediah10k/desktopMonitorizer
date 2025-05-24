package org.proy.monitorizerdesktop.clientserver.classes.client;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@Component
public class TransmisorVideo {

    private Socket socket;
    private DataOutputStream dos;
    private volatile boolean transmitiendo;
    private Thread hiloTransmision;
    private CapturadorVideo capturador;


    public void setProperties(String host, Integer puerto) {
        try {
            socket = new Socket(host, puerto);
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error al establecer conexión para transmisión", e);
        }
    }

    public void iniciarTransmision() {
        transmitiendo = true;
        hiloTransmision = new Thread(this::transmitirVideo);
        hiloTransmision.start();
    }

    public void transmitirVideo() {
        try{
            capturador = new CapturadorVideo();
            capturador.iniciarCaptura();
            while (transmitiendo) {
                BufferedImage screen= capturador.capturarVideo();
                if(screen==null){
                    System.err.println("Frame capturado es null. Posiblemente se detuvo la captura.");
                   transmitiendo = false;
                    break;
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(screen, "jpg", baos);
                byte[] imageBytes = baos.toByteArray();

                dos.writeInt(imageBytes.length);
                dos.write(imageBytes);
                dos.flush();
            }

        }catch(IOException e){
            System.err.println("Captura finalizada o fallida: " + e.getMessage());
        }
    }

    public void detenerTransmision() {
        capturador.detenerCaptura();
        transmitiendo = false;
        hiloTransmision.interrupt();

        try {
            if (hiloTransmision != null) hiloTransmision.join(1000);
            if (dos != null) dos.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (Exception e) {
            System.err.println("Error al cerrar transmisión: " + e.getMessage());
        }
    }
}
