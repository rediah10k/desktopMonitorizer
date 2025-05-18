package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class ReceptorVideo {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dis;
    private Boolean transmitiendo;
    private Integer puerto;

    public void setPuerto(Integer puerto) {
        this.puerto = puerto;
    }

    public Boolean isTransmitiendo() {
        return transmitiendo;
    }

    public void setTransmitiendo(Boolean transmitiendo) {
        this.transmitiendo = transmitiendo;
    }

    public Integer getPuerto() {
        return puerto;
    }

   public void setProperties(){
       cerrar();
       try{
           serverSocket = new ServerSocket(puerto);
           socket = serverSocket.accept();
           dis = new DataInputStream(socket.getInputStream());
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }


    public BufferedImage iniciarRecepcion() {
     try{
         while (true) {
             int longitud = dis.readInt();
             byte[] datos = new byte[longitud];
             dis.readFully(datos);
             ByteArrayInputStream bais = new ByteArrayInputStream(datos);
             BufferedImage imagen = ImageIO.read(bais);
             return imagen;

         }
     } catch (IOException e) {
         throw new RuntimeException(e);
     }

    }

    public void cerrar() {
        try { if (dis != null) dis.close(); } catch (IOException ignored) {}
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        try { if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close(); } catch (IOException ignored) {}
    }

}

