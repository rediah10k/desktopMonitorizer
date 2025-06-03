package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;


@Component
public class TransmisorArchivos {
 private Socket socket;

    public void solicitarEnvio(String ip, Integer puerto){
        try{
            socket = new Socket(ip, puerto);
        }catch (Exception e){
            System.out.println("La conexion para transmitir archivos no sirve");
        }
    }

    public void enviarArchivo(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                dos.writeUTF(file.getName());
                dos.writeLong(file.length());

                byte[] buffer = new byte[4096];
                int read;
                while ((read = fis.read(buffer)) > 0) {
                    dos.write(buffer, 0, read);
                }



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Se ha cerrado la conexion de transferencia de archivos");
                }
            } catch (IOException e) {
                System.out.println("Error al cerrar la conexión de transferencia de archivos: " + e.getMessage());
            }
        }
    }
}

