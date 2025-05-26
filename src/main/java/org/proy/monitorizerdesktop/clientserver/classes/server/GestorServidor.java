package org.proy.monitorizerdesktop.clientserver.classes.server;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.proy.monitorizerdesktop.clientserver.utils.EstatusConexion;
import org.proy.monitorizerdesktop.clientserver.views.ServidorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.List;

@Component
public class GestorServidor {
    private Thread recibirTransmision;
    private ReceptorVideo receptor;
    private ServidorListener servidorListener;
    private PoolConexiones poolConexiones;

    @Autowired
    public GestorServidor(ReceptorVideo receptor, PoolConexiones poolConexiones) {
        this.receptor = receptor;
        this.poolConexiones = poolConexiones;
    }

    public void setServidorListener(ServidorView servidorView) {
        this.servidorListener = new ServidorListener(servidorView);
    }

    public ReceptorVideo getReceptor() {
        return receptor;
    }

    public PoolConexiones getPoolConexiones() {
        return poolConexiones;
    }

    public List<Conexion> getPoolConexionesDisponibles() {
        return poolConexiones.getDisponibles();
    }

    public List<Conexion> getPoolConexionesOcupadas() {
        return poolConexiones.getOcupadas();
    }

    public Integer getPuertoVideo() {
        return this.getReceptor().getPuerto();
    }

    public void setPuertoVideo(Integer puertoVideo) {
        this.getReceptor().setPuerto(puertoVideo);
    }

    public void agregarCliente(ConexionDTO conexionNueva) {
        poolConexiones.obtenerConexion(conexionNueva);
        servidorListener.onConexionAceptada();
    }

    public void desconectarCliente(Conexion conexion) {
        cerrarTransmision(conexion);
        poolConexiones.liberarConexion(conexion);
        servidorListener.onConexionCerrada();

    }

    public Conexion buscarCliente(ConexionDTO conexionSolicitada) {
        return poolConexiones.obtenerConexionPorIp(conexionSolicitada.getIp());
    }


    public void solicitarTransmision(Conexion conexion) {
       conexion.transmitirInformacion(EstatusConexion.INICIAR_TRANSMISION.name() + " PUERTO: "+this.getPuertoVideo());
       receptor.setProperties();
       receptor.setTransmitiendo(true);
       recibirTransmision = new Thread(this::actualizarTransmision);
       recibirTransmision.start();
    }

    public void cerrarTransmision(Conexion conexion) {
        if(recibirTransmision != null) {
            recibirTransmision.interrupt();
            receptor.setTransmitiendo(false);
            receptor.detenerGrabacion();
            conexion.transmitirInformacion(EstatusConexion.DETENER_TRANSMISION.name());
            servidorListener.onTransmisionCerrada();

        }
    }


    public void actualizarTransmision() {
        while(receptor.isTransmitiendo()) {
            BufferedImage frame = receptor.iniciarRecepcion();
            if(frame == null) {
                return;
            }
            servidorListener.onTransmision(frame);
        }

    }
}
