package org.proy.monitorizerdesktop.clientserver.classes.client;


import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.utils.EstatusConexion;
import org.proy.monitorizerdesktop.clientserver.views.ClienteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Component
public class GestorCliente {
    private Integer puerto;
    private DataInputStream dis;
    private ServerSocket serverSocket;
    private Socket socket;
    private ClienteListener clienteListener;
    private TransmisorVideo transmisorVideo;
    private TransmisorEventos transmisorEventos;
    private ReceptorArchivos receptorArchivos;


     public Integer getPuerto() {
         return puerto;
     }

     public void setPuerto(Integer puerto) {
         this.puerto = puerto;
     }

    @Autowired
    public GestorCliente(  TransmisorEventos transmisorEventos, TransmisorVideo transmisorVideo, ReceptorArchivos receptorArchivos) {
        this.transmisorEventos = transmisorEventos;
        this.transmisorVideo = transmisorVideo;
        this.receptorArchivos = receptorArchivos;

    }

    public void setClienteListener(ClienteView view) {
        clienteListener = new ClienteListener(view);
    }

    public void esperarConexion(UsuarioDTO usuario) {
        try {
            this.serverSocket = new ServerSocket(puerto);
            System.out.println("Esperando conexión del servidor...");
            socket = serverSocket.accept();
            System.out.println("Conexión establecida desde el servidor: " + socket.getInetAddress());
            clienteListener.onConexionAceptada();
            enviarIdCliente(usuario);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void escucharServidor() {
        try {
            dis = new DataInputStream(socket.getInputStream());
            while (true) {
                int longitud = dis.readInt();
                byte[] buffer = new byte[longitud];
                dis.readFully(buffer);
                String mensaje = new String(buffer, StandardCharsets.UTF_8);
                    procesarMensaje(mensaje);
                Thread.sleep(100);
            }
        } catch (Exception e) {
            System.out.println("Error al leer del servidor o conexión cerrada: " + e.getMessage());
            cerrarConexion();
            clienteListener.onConexionCerrada();
        }
    }

    private void procesarMensaje(String mensaje) {
        if(mensaje.startsWith(EstatusConexion.INICIAR_TRANSMISION.name())) {
            Integer puerto = extraerPuerto(mensaje);
            transmisorVideo.setProperties(socket.getInetAddress().getHostAddress(), puerto);
            transmisorEventos.setProperties(socket.getInetAddress().getHostAddress(), 2535);
           transmisorVideo.iniciarTransmision();
            transmisorEventos.iniciarTransmision();
            clienteListener.onTransmision();
        }if(mensaje.equals(EstatusConexion.DETENER_TRANSMISION.name())) {
            transmisorVideo.detenerTransmision();
            transmisorEventos.detenerTransmision();
            clienteListener.onConexionAceptada();
        }if(mensaje.equals(EstatusConexion.INICIAR_ARCHIVO.name())) {
            new Thread(() -> {receptorArchivos.ponerEnEscucha();}).start();
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
            if (socket != null && !socket.isClosed()) {
                socket.close();
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

    public void enviarIdCliente(UsuarioDTO usuario) {
        try{
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            String id = String.valueOf(usuario.getId());
            byte[] datos = id.getBytes(StandardCharsets.UTF_8);
            dos.writeInt(datos.length);
            dos.write(datos);
            dos.flush();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        new Thread(this::escucharServidor).start();

}


}



