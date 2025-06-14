package org.proy.monitorizerdesktop.clientserver.classes.server;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.proy.monitorizerdesktop.clientserver.dtos.SesionDTO;
import org.proy.monitorizerdesktop.clientserver.utils.EstatusConexion;
import org.proy.monitorizerdesktop.clientserver.views.ServidorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Component
public class GestorServidor {

    private Thread hiloRecibirVideo;
    private Thread hiloRecibirEventos;
    private TransmisorArchivos transmisorArchivos;
    private ReceptorVideo receptorVideo;
    private ReceptorEventos receptorEventos;
    private ServidorListener servidorListener;
    private PoolConexiones poolConexiones;

    @Autowired
    public GestorServidor(TransmisorArchivos transmisorArchivos, ReceptorVideo receptorVideo,ReceptorEventos receptorEventos, PoolConexiones poolConexiones) {
        this.receptorVideo = receptorVideo;
        this.receptorEventos = receptorEventos;
         this.poolConexiones = poolConexiones;
         this.transmisorArchivos = transmisorArchivos;
    }

    public ReceptorVideo getReceptorVideo() {
        return receptorVideo;
    }

    public void setServidorListener(ServidorView servidorView) {
        this.servidorListener = new ServidorListener(servidorView);
    }

    public PoolConexiones getPoolConexiones() {
        return poolConexiones;
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
        hiloRecibirVideo = new Thread(this::recibirFrames);
        hiloRecibirEventos = new Thread(this::mostrarEventos);
        hiloRecibirVideo.start();
        hiloRecibirEventos.start();
        conexion.transmitirInformacion(EstatusConexion.INICIAR_TRANSMISION.name() + " PUERTO: "+this.getPuertoVideo());

    }

    public void cerrarTransmision(Conexion conexion) {
        if(hiloRecibirVideo != null) {
            hiloRecibirVideo.interrupt();
            conexion.transmitirInformacion(EstatusConexion.DETENER_TRANSMISION.name());
            receptorVideo.setTransmitiendo(false);
            receptorVideo.detenerGrabacion();
            receptorEventos.setTransmitiendo(false);
            receptorEventos.detenerRecepcion();
            servidorListener.onTransmisionCerrada();
        }
    }

    public File almacenanarVideoTransmitido() {

        return receptorVideo.almacenarVideoTransmitido();
    }

    public void recibirFrames() {
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

    public void enviarArchivosAClientes(File archivo) {
        for (Conexion c : getPoolConexionesOcupadas()) {
            c.transmitirInformacion(EstatusConexion.INICIAR_ARCHIVO.name());
             transmisorArchivos.solicitarEnvio(c.getIp(),2636);
            new Thread(() -> {
                transmisorArchivos.enviarArchivo(archivo);}).start();

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
