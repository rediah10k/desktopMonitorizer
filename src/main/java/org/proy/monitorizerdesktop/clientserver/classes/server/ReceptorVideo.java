package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.proy.monitorizerdesktop.clientserver.dtos.SesionDTO;
import org.proy.monitorizerdesktop.clientserver.utils.GeneradorVideoLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class ReceptorVideo {
    private ServerSocket serverSocket;
    private GeneradorVideoLocal generadorVideoLocal;
    private Socket socket;
    private DataInputStream dis;
    private Boolean transmitiendo;
    private Integer puerto;
    private Integer fps=60;

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
           generadorVideoLocal = new GeneradorVideoLocal();
           generadorVideoLocal.setFps(fps);

       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }


    public BufferedImage iniciarRecepcion() {
     try{
             int longitud = dis.readInt();
             byte[] datos = new byte[longitud];
             dis.readFully(datos);
             ByteArrayInputStream bais = new ByteArrayInputStream(datos);
             BufferedImage imagen = ImageIO.read(bais);
              if(imagen == null){
                System.out.println("Frame capturado es nulo. Posiblemente se detuvo la transmision.");
                return null;
             }
             if (generadorVideoLocal.getHeight() == 0 || generadorVideoLocal.getWidth() == 0) {
                 generadorVideoLocal.setHeight(imagen.getHeight());
                 generadorVideoLocal.setWidth(imagen.getWidth());
                 generadorVideoLocal.setRecorderProperties();
             }
             generadorVideoLocal.anadirFrame(imagen);
             return imagen;

     } catch (Exception e) {
         System.out.println("Captura finalizada o fallida: " + e.getMessage());
     }
        return null;
    }

    public void detenerGrabacion() {
        if (generadorVideoLocal != null) {
            try {
                generadorVideoLocal.detenerGeneracion();
            } catch (Exception e) {
                System.out.println("Error al detener grabación: " + e.getMessage());
            }
        }
    }

    public File almacenarVideoTransmitido() {
        return generadorVideoLocal.guardarVideo("servermedia");
    }


    public void cerrar() {
        try { if (dis != null) dis.close(); } catch (IOException ignored) {}
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        try { if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close(); } catch (IOException ignored) {}
    }

}

