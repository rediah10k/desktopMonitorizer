package org.proy.monitorizerdesktop.auth.views;

import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.main.views.ClienteView;
import org.proy.monitorizerdesktop.main.views.ServidorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class VistaFactory {
    private final ClienteView clienteView;
    private final ServidorView servidorView;

    @Autowired
    public VistaFactory(ClienteView clienteView, ServidorView servidorView) {
        this.clienteView = clienteView;
        this.servidorView = servidorView;
    }

    public JFrame getVistaPorRol(String rol, Usuario usuario) {
        if ("Cliente".equalsIgnoreCase(rol)) {
            clienteView.setUsuario(usuario);
            return clienteView;
        } else if ("Servidor".equalsIgnoreCase(rol)) {
            servidorView.setUsuario(usuario);
            return servidorView;
        }
        throw new IllegalArgumentException("Rol desconocido");
    }
}
