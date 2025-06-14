package org.proy.monitorizerdesktop.clientserver.classes.server;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Builder
@Getter
@Setter
public class Conexion implements Runnable {

    private Socket socket;

    private Boolean activo;
    private Long clienteId;


    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getIp(){
        return socket.getInetAddress().getHostAddress();
    }

    public Integer getPort(){
        return socket.getPort();
    }

    public Long getClienteId(){
        return clienteId;
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
        recibirIdCliente();
        while (activo) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
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

    public void recibirIdCliente(){
        try{
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int longitud = dis.readInt();
            byte[] buffer = new byte[longitud];
            dis.readFully(buffer);
            clienteId = Long.valueOf(new String(buffer, StandardCharsets.UTF_8));
            System.out.println("ID del cliente conectado: " + clienteId);
        }catch(Exception e){
            System.out.println("Error al recibir idCliente: " + e.getMessage());
        }

    }

    }

