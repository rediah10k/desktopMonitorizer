package org.proy.monitorizerdesktop.clientserver.controllers;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.IController;
import org.proy.monitorizerdesktop.clientserver.classes.server.Conexion;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.classes.server.Servidor;
import org.proy.monitorizerdesktop.clientserver.services.UserService;
import org.proy.monitorizerdesktop.clientserver.views.ServidorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class ServidorController implements IController {

    private final Servidor servidor;
    private final UserService userService;

    @Autowired
    public ServidorController(Servidor servidor, UserService userService) {
        this.servidor = servidor;
        this.userService = userService;
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
   }

}
