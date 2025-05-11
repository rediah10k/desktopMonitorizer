package org.proy.monitorizerdesktop.auth.views;

import javax.swing.*;
import java.awt.*;

public class ClienteView extends JFrame {

    public ClienteView() {
        buildUI();
    }

    private void buildUI() {
        setTitle("Cliente - Esperando Conexiones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JLabel mensaje = new JLabel("A la espera de conexiones", SwingConstants.CENTER);
        mensaje.setFont(new Font("Arial", Font.PLAIN, 16));
        add(mensaje);

        setVisible(true);
    }
}

