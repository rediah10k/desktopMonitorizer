package org.proy.monitorizerdesktop.clientserver.classes.server;

import java.io.*;
import java.net.Socket;

public class Conexion implements Runnable {

    private GestorServidor gestor;
    private Socket socket;
    private volatile Boolean activo;

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getIp(){
        return socket.getInetAddress().getHostAddress();
    }

    public Integer getPort(){
        return socket.getPort();
    }


    public Conexion(Socket socket, GestorServidor gestor) {
        this.gestor = gestor;
        this.socket = socket;
    }

    public void cerrarConexion() {
       gestor.cerrarTransmision(this);
       gestor.notificarDesconexion(this);
       this.activo = false;

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                System.out.println(" Conexión cerrada con el cliente: " + socket.getInetAddress().getHostAddress());
            }
        } catch (IOException e) {
            System.out.println(" Error al cerrar la conexión: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        activo = true;
        while (activo) {
            mantenerCanal("");
        }
        System.out.println("Conexión establecida con: " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
    }


    public void mantenerCanal(String mensaje) {
        try {
            OutputStream out = socket.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            byte[] datos = mensaje.getBytes();

            dos.writeInt(datos.length);
            dos.write(datos);
            dos.flush();

        } catch (IOException e) {
            System.out.println("Cliente desconectado: ");
        }
    }

    }

