package org.proy.monitorizerdesktop.clientserver.classes.server;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.proy.monitorizerdesktop.clientserver.dtos.SesionDTO;
import org.proy.monitorizerdesktop.clientserver.utils.EstatusConexion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.List;

@Component
public class GestorServidor {

    private Thread hiloRecibirVideo;
    private Thread hiloRecibirEventos;

    private ReceptorVideo receptorVideo;
    private ReceptorEventos receptorEventos;
    private ServidorListener servidorListener;
    private PoolConexiones poolConexiones;

    @Autowired
    public GestorServidor(ReceptorVideo receptorVideo,ReceptorEventos receptorEventos, ServidorListener servidorHandler, PoolConexiones poolConexiones) {
        this.receptorVideo = receptorVideo;
        this.servidorListener = servidorHandler;
        this.poolConexiones = poolConexiones;
        this.receptorEventos = receptorEventos;
    }

    public ReceptorVideo getReceptorVideo() {
        return receptorVideo;
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
        return this.getReceptorVideo().getPuerto();
    }

    public void setPuertoVideo(Integer puertoVideo) {
        this.getReceptorVideo().setPuerto(puertoVideo);
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
        hiloRecibirVideo = new Thread(this::actualizarTransmisionFrames);
        hiloRecibirEventos = new Thread(this::mostrarEventos);
        hiloRecibirVideo.start();
        hiloRecibirEventos.start();
    }

    public void cerrarTransmision(Conexion conexion) {
        if(hiloRecibirVideo != null) {
            hiloRecibirVideo.interrupt();
            receptorVideo.setTransmitiendo(false);
            receptorVideo.detenerGrabacion();
            conexion.transmitirInformacion(EstatusConexion.DETENER_TRANSMISION.name());
            servidorListener.onTransmisionCerrada();
        }
    }

    public void actualizarTransmisionFrames() {
        receptorVideo.setProperties();
        receptorVideo.setTransmitiendo(true);
        while(receptorVideo.isTransmitiendo()) {
            BufferedImage frame = receptorVideo.iniciarRecepcion();
            if(frame == null) {
                return;
            }
            servidorListener.onTransmision(frame);
        }

    }

    public void mostrarEventos(){
        receptorEventos.setProperties();
        receptorEventos.setTransmitiendo(true);
        while(receptorEventos.isTransmitiendo()){
            String mensaje=receptorEventos.leerEventoRecibido();
            System.out.println(mensaje);
        }
    }
}
