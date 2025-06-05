package org.proy.monitorizerdesktop.clientserver.classes.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Component
@Getter
@Setter
public class ReceptorArchivos {

    private static final int PUERTO = 2636;
    private volatile boolean recibiendo = false;

    public void ponerEnEscucha() {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(PUERTO));
            System.out.println("Esperando conexión en el puerto " + PUERTO + "...");

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Cliente conectado desde: " + socket.getInetAddress());
                iniciarRecepcion(socket);
            }

        } catch (IOException e) {
            System.out.println("Error al poner en escucha o aceptar conexión: " + e.getMessage());
            // Si usas una interfaz base de manejo de errores, aquí lanzarías una excepción custom si aplica
        }
    }

    private void iniciarRecepcion(Socket socket) {
        System.out.println("Iniciando recepción de archivo");
        recibiendo = true;

        try (DataInputStream dis = new DataInputStream(socket.getInputStream())) {
            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            File file = new File("recibidos/" + fileName);
            file.getParentFile().mkdirs();

            try (FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                long totalRead = 0;

                while (totalRead < fileSize) {
                    int read = dis.read(buffer, 0, (int) Math.min(buffer.length, fileSize - totalRead));
                    if (read == -1) break;
                    fos.write(buffer, 0, read);
                    totalRead += read;
                }
            }

            System.out.println("Archivo recibido: " + fileName);
        } catch (IOException e) {
            System.out.println("Error durante la recepción del archivo: " + e.getMessage());
        } finally {
            recibiendo = false;
            System.out.println("Recepción finalizada y socket cerrado.");
        }
    }
}
