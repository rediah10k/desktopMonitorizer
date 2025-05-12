package org.proy.monitorizerdesktop.main.views;

import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.main.classes.Cliente;
import org.proy.monitorizerdesktop.main.controllers.ClienteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@Component
public class ClienteView extends JFrame {

    private ClienteController clienteController;

    @Autowired
    public ClienteView(ClienteController clienteController) {
        this.clienteController = clienteController;
        buildUI();
    }

    public void setUsuario(Usuario usuario) {
        this.clienteController.setUsuario(usuario);

    }

    private void buildUI() {
        setTitle("Cliente - Esperando Conexiones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JLabel mensaje = new JLabel("A la espera de conexiones", SwingConstants.CENTER);
        mensaje.setFont(new Font("Arial", Font.PLAIN, 16));
        add(mensaje);

    }
}

