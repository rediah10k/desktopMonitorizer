package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.proy.monitorizerdesktop.clientserver.utils.EstatusConexion;
import org.springframework.context.ApplicationEventPublisher;

import java.io.*;
import java.net.Socket;

public class Conexion implements Runnable {

    private Socket socket;
    private volatile Boolean activo;

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getIp(){
        return socket.getInetAddress().getHostAddress();
    }



    public Integer getPort(){
        return socket.getPort();
    }


    public Conexion(Socket socket) {
        this.socket = socket;
    }

    public void cerrarConexion() {

       this.activo = false;

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println("Conexión cerrada con el cliente: " + socket.getInetAddress().getHostAddress());
            }
        } catch (IOException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        activo = true;
        while (activo) {
            transmitirInformacion(EstatusConexion.PING.name());
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Conexión establecida con: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
    }


    public void transmitirInformacion(String mensaje) {
        try {
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            byte[] datos = mensaje.getBytes();
            dos.writeInt(datos.length);
            dos.write(datos);
            dos.flush();

        } catch (IOException e) {
            System.out.println("Cliente desconectado: ");
            cerrarConexion();
        }
    }

    }

