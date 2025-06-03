package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.springframework.stereotype.Component;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class PoolConexiones {

    private List<Conexion> disponibles = new ArrayList<>();
    private List<Conexion> ocupadas = new ArrayList<>();
    private Integer maxConexiones=2;
    private ConexionFactory conexionFactory;

    public PoolConexiones(ConexionFactory conexionFactory) {
        this.conexionFactory = conexionFactory;
    }

    public Integer getMaxConexiones() {
        return maxConexiones;
    }

    public List<Conexion> getDisponibles() {
        return disponibles;
    }

    public List<Conexion> getOcupadas() {
        return ocupadas;
    }


    public void obtenerConexion(ConexionDTO clienteNuevo) {
        Conexion conexionNueva = null;

        if(ocupadas.size() >= maxConexiones) {
            System.out.println("No se puede agregar un nuevo cliente, la lista esta llena");
        }

        if (!disponibles.isEmpty()) {
            conexionNueva = disponibles.remove(0);
            Socket newSocket = conexionFactory.generarSocket(clienteNuevo);
            conexionNueva.setSocket(newSocket);
            ocupadas.add(conexionNueva);
            System.out.println("Se ha reutilizado una conexion");

        }

        if (ocupadas.size()+disponibles.size()< maxConexiones) {
            conexionNueva = conexionFactory.crearConexionNueva(clienteNuevo);
            ocupadas.add(conexionNueva);
            System.out.println("Se ha creado una conexion");

        }
        new Thread(conexionNueva).start();
    }

    public void liberarConexion(Conexion conexion) {
        if (conexion != null) {
            conexion.cerrarConexion();
            ocupadas.remove(conexion);
            disponibles.add(conexion);
        }
    }

    public Conexion obtenerConexionPorIp(String ip) {
        for (Conexion c : ocupadas) {
            if (c.getIp().equals(ip) ) {
                return c;
            }
        }
        return null;
    }


    public synchronized void cerrarTodo() {
        for (Conexion c : disponibles) c.cerrarConexion();
        for (Conexion c : ocupadas) c.cerrarConexion();
        disponibles.clear();
        ocupadas.clear();
    }



}
