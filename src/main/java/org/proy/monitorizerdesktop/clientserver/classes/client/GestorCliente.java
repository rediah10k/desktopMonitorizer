package org.proy.monitorizerdesktop.clientserver.classes.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
public class GestorCliente implements Runnable {

    @Getter
    @Setter
    private Integer puerto;
    private ServerSocket serverSocket;
    private Socket conexion;
    private ClienteListener clienteListener;
    private TransmisorVideo transmisorVideo;


    @Autowired
    public GestorCliente(ClienteListener clienteListener, TransmisorVideo transmisorVideo) {
        this.clienteListener = clienteListener;
        this.transmisorVideo = transmisorVideo;
    }

    public void iniciarSocketServer() {
        try {
            this.serverSocket = new ServerSocket(puerto);
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Esperando conexión del servidor...");
            conexion = serverSocket.accept();
            System.out.println("Conexión establecida desde el servidor: " + conexion.getInetAddress());
            clienteListener.onConexionAceptada();
            new Thread(this::escucharServidor).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escucharServidor() {
        try {
            DataInputStream dis = new DataInputStream(conexion.getInputStream());

            while (true) {
                int longitud = dis.readInt();
                byte[] buffer = new byte[longitud];
                dis.readFully(buffer);

                String mensaje = new String(buffer, StandardCharsets.UTF_8);
                if (!mensaje.isEmpty()) {
                    procesarMensaje(mensaje);
                }

            }

        } catch (IOException e) {
            System.out.println("Error al leer del servidor o conexión cerrada: " + e.getMessage());
            cerrarConexion();
            clienteListener.onConexionCerrada();
        }
    }


    private void procesarMensaje(String mensaje) {
        System.out.println(mensaje);
        if(mensaje.startsWith("INICIAR_TRANSMISION")) {
            System.out.println("INICIAR TRANSMISION");
            Integer puerto = extraerPuerto(mensaje);
            clienteListener.onTransmision();
            transmisorVideo.setProperties(conexion.getInetAddress().getHostAddress(), puerto);
           transmisorVideo.iniciarTransmision();

        }if(mensaje.equals("FINALIZAR_TRANSMISION")) {
            System.out.println("FINALIZAR TRANSMISION");
            clienteListener.onConexionAceptada();
            transmisorVideo.detenerTransmision();

        }
    }

    private Integer extraerPuerto(String mensaje) {
        int puerto = 4096;
        String[] partes = mensaje.split("PUERTO: ");
        if (partes.length == 2) {
            try {
                puerto = Integer.parseInt(partes[1].trim());
            } catch (NumberFormatException e) {
                System.err.println("Puerto inválido, usando por defecto: 4096");
            }
        }
        return puerto;
    }




    public void cerrarConexion() {

        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Socket de escucha cerrado.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
