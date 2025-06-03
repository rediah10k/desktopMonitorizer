package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.Socket;

@Component
public class ConexionFactory {


    public Socket generarSocket(ConexionDTO clienteNuevo) {
        try{
            return new Socket(clienteNuevo.getIp(),clienteNuevo.getPuerto());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Conexion crearConexionNueva(ConexionDTO clienteNuevo) {
        Socket socket = generarSocket(clienteNuevo);
        return Conexion.builder().socket(socket).build();
    }
}
