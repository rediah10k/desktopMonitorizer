package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.proy.monitorizerdesktop.clientserver.utils.EstatusConexion;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
public class ReceptorEventos {
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream dis;
    private Boolean transmitiendo;
    private Integer puerto=2535;

    public Boolean isTransmitiendo() {
        return transmitiendo;
    }
    public void setTransmitiendo(Boolean transmitiendo) {
        this.transmitiendo = transmitiendo;
    }

    public void setProperties(){
        cerrar();
        try{
            serverSocket = new ServerSocket(puerto);
            System.out.println("A la espera para recepcion de eventos en el puerto: " + puerto);
            socket = serverSocket.accept();
            dis = new DataInputStream(socket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String leerEventoRecibido() {
        try {
                int longitud = dis.readInt();
                byte[] buffer = new byte[longitud];
                dis.readFully(buffer);
                String mensaje = new String(buffer, StandardCharsets.UTF_8);
                if (!mensaje.equals(EstatusConexion.PING.name())) {
                    return mensaje;
                }
                Thread.sleep(100);
        } catch (Exception e) {
            System.out.println("Error al leer del servidor o conexión cerrada: " + e.getMessage());
        }
        return "";
    }

    public void detenerRecepcion(){
        transmitiendo = false;
    }

    public void cerrar() {
        try { if (dis != null) dis.close(); } catch (IOException ignored) {}
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException ignored) {}
        try { if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close(); } catch (IOException ignored) {}
    }

}
