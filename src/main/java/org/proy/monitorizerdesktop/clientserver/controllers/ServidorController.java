package org.proy.monitorizerdesktop.clientserver.controllers;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.IController;
import org.proy.monitorizerdesktop.clientserver.classes.server.Conexion;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.proy.monitorizerdesktop.clientserver.dtos.SesionDTO;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.classes.server.Servidor;
import org.proy.monitorizerdesktop.clientserver.services.UserService;
import org.proy.monitorizerdesktop.clientserver.services.VideoService;
import org.proy.monitorizerdesktop.clientserver.views.ServidorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Component
public class ServidorController implements IController {

    private final Servidor servidor;
    private final VideoService videoService;

    @Autowired
    public ServidorController(Servidor servidor, VideoService videoService) {
        this.servidor = servidor;
        this.videoService = videoService;
    }

    @Override
    public Integer getPuerto() {
        return servidor.getGestorServidor().getPuertoVideo();
    }

    @Override
    public void setPuerto(Integer puerto) {
        servidor.getGestorServidor().setPuertoVideo(puerto);
    }


    @Override
    public UsuarioDTO getUsuario() {
        return servidor.getUsuario();
    }


    public void suscribirseAListener(ServidorView view) {
        servidor.getGestorServidor().setServidorListener(view);
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.servidor.setUsuario(usuario);
    }

    public void agregarCliente(String ip, Integer puerto) {
        ConexionDTO nuevaConexion = new ConexionDTO(ip, puerto);
        servidor.getGestorServidor().agregarCliente(nuevaConexion);

    }

    public void eliminarCliente(ConexionDTO clienteListado) {
        Conexion clienteConectado = servidor.getGestorServidor().buscarCliente(clienteListado);
        servidor.getGestorServidor().desconectarCliente(clienteConectado);
    }

    public void establecerMaximoConexiones(Integer maxConexiones) {
        servidor.getListConexiones().getPoolConexiones().setMaxConexiones(maxConexiones);
    }

    public List<ConexionDTO> obtenerUsuariosConectados() {
        List<Conexion> clientes =servidor.getGestorServidor().getPoolConexionesOcupadas();
        List<ConexionDTO> clienteDTOs = new ArrayList<>();

        for (Conexion cliente : clientes) {
            clienteDTOs.add(new ConexionDTO(cliente.getIp(),cliente.getPort()));
        }
        return clienteDTOs;

    }

    public Integer getMaximasConexiones() {
        return servidor.getGestorServidor().getPoolConexiones().getMaxConexiones();
    }

    public void solicitarTransmision(ConexionDTO clienteListado) {
       Conexion clienteConectado = servidor.getGestorServidor().buscarCliente(clienteListado);
        servidor.getGestorServidor().solicitarTransmision(clienteConectado);
    }

   public void cerrarTransmision(ConexionDTO clienteListado) {
       Conexion clienteConectado = servidor.getGestorServidor().buscarCliente(clienteListado);
       servidor.getGestorServidor().cerrarTransmision(clienteConectado);

       SesionDTO sesionGenerada= new SesionDTO(clienteConectado.getClienteId(),servidor.getUsuario().getId());
       File videoGuardadoLocal = servidor.getGestorServidor().almacenanarVideoTransmitido();
       videoService.persistirVideoGenerado(videoGuardadoLocal, sesionGenerada);
   }
<<<<<<< HEAD

   public void enviarArchivoAClientes(File archivo) {
       servidor.getGestorServidor().enviarArchivosAClientes(archivo);
   }

=======
>>>>>>> 392907795abe34b8a9a78753e7dc5ea9489653b5
}
