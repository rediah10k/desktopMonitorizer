package org.proy.monitorizerdesktop.clientserver.classes.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@Getter
@Setter
public class ReceptorArchivos {
  private volatile Boolean recibiendo;
    private ServerSocket serverSocket;
    private Socket socket;

    public void ponerEnEscucha(){
        try {
            this.serverSocket = new ServerSocket(2636);
            socket = serverSocket.accept();
            iniciarRecepcion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void iniciarRecepcion(){
        System.out.println("Recibiendo archivo");
        recibiendo = true;
        while (recibiendo) {
            try (
                    DataInputStream dis = new DataInputStream(socket.getInputStream())) {
                String fileName = dis.readUTF();
                long fileSize = dis.readLong();

                File file = new File("recibidos/" + fileName);
                file.getParentFile().mkdirs();

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    byte[] buffer = new byte[4096];
                    int read;
                    long totalRead = 0;
                    while ((read = dis.read(buffer, 0, (int)Math.min(buffer.length, fileSize - totalRead))) > 0) {
                        fos.write(buffer, 0, read);
                        totalRead += read;
                        if (totalRead >= fileSize) break;
                    }
                }
                System.out.println("Archivo recibido: " + fileName);
                recibiendo = false;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
