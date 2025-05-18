package org.proy.monitorizerdesktop.clientserver.classes.server;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class GestorServidor {
    private Thread recibirTransmision;
    private List<Conexion> clientes = new ArrayList<>();
    private Integer maxConexiones=10;
    private ReceptorVideo receptor;
    private ServidorListener servidorListener;


    @Autowired
    public GestorServidor(ReceptorVideo receptor, ServidorListener servidorHandler) {
        this.receptor = receptor;
        this.servidorListener = servidorHandler;
    }

    public ReceptorVideo getReceptor() {
        return receptor;
    }

    public List<Conexion> getClientes() {
        return clientes;
    }

    public Integer getMaxConexiones() {
        return maxConexiones;
    }

    public Integer getPuertoVideo() {
        return this.getReceptor().getPuerto();
    }

    public void setPuertoVideo(Integer puertoVideo) {
        this.getReceptor().setPuerto(puertoVideo);
    }


    public Conexion crearClienteConectado(ConexionDTO clienteNuevo) {
        try{
            Socket socket = new Socket(clienteNuevo.getIp(),clienteNuevo.getPuerto());
            return new Conexion(socket, this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public void anadirCliente(Conexion conexion ) {
        if(clientes.size() >= maxConexiones) {
            System.out.println("Maxima capacidad de clientes alcanzada, no se permiten conexiones nuevas");
            return;
        }
        clientes.add(conexion);
        new Thread(conexion).start();

    }

    public void quitarCliente(Conexion conexion) {
        if (conexion != null) {
            conexion.cerrarConexion();
        }
    }

    public Conexion obtenerConexionPorIp(String ip) {
        for (Conexion c : clientes) {
            if (c.getIp().equals(ip) ) {
                return c;
            }
        }
        return null;
    }


    public void notificarDesconexion(Conexion conexion) {
        clientes.remove(conexion);
    }


    public void solicitarTransmision(Conexion conexion) {
       conexion.mantenerCanal("INICIAR_TRANSMISION");
       receptor.setProperties();
       receptor.setTransmitiendo(true);
       recibirTransmision = new Thread(this::actualizarTransmision);
       recibirTransmision.start();


    }

    public void cerrarTransmision(Conexion conexion) {
        receptor.setTransmitiendo(false);
        conexion.mantenerCanal("FINALIZAR_TRANSMISION");
        servidorListener.onTransmisionCerrada();
        recibirTransmision.interrupt();
    }


    public void actualizarTransmision() {
        while(receptor.isTransmitiendo()) {
            BufferedImage frame = receptor.iniciarRecepcion();
            servidorListener.onTransmision(frame);

        }

    }
}
