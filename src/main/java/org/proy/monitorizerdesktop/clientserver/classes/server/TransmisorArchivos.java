package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

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
                System.out.println("Archivo enviado con éxito.");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

