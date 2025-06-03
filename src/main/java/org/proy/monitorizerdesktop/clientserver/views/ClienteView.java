package org.proy.monitorizerdesktop.clientserver.views;

import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.controllers.ClienteController;

import org.proy.monitorizerdesktop.clientserver.utils.IpPropiedades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.io.File;


@Component
public class ClienteView extends JFrame {

    JPanel panel;
    private ClienteController controller;
    private JTextArea estadoActual;
    private PuertoView puertoView;

    @Autowired
    public ClienteView(ClienteController clienteController, PuertoView puertoView) {
        this.controller = clienteController;
        this.puertoView = puertoView;
        controller.suscribirseAListener(this);
        setConfigBase();

    }

    public void inicializarVentana(){
        puertoView.pedirPuerto();
        mostrarInterfazEscucha();

    }

    private void setConfigBase(){
        estadoActual = new JTextArea();
        estadoActual.setEditable(false);
        estadoActual.setOpaque(false);
        estadoActual.setBorder(null);
        estadoActual.setFocusable(false);
        panel = new JPanel(new GridLayout(2, 1));
        setSize(590, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        puertoView.setParentComponents(this, controller);

    }

    public void setUsuario(UsuarioDTO usuario) {
        this.controller.setUsuario(usuario);
    }

    private void mostrarInterfazEscucha(){
        this.controller.iniciarCliente();
        vistaEspera();
    }



    public void vistaEspera(){
        construirUI();
        String ip= new IpPropiedades().obtenerIP();
        setTitle("Cliente - Esperando Conexiones");

        estadoActual.setText( "Direccion IP: " +ip+"\nA la espera de conexiones en el puerto " + this.controller.getPuerto());
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(estadoActual,BorderLayout.CENTER);

        panel.revalidate();
        panel.repaint();
    }

    public void vistaConectada(){
        construirUI();
        setTitle("Cliente - Conectado ");
        estadoActual.setText("Conexion exitosa con el servidor, sin transmitir en este momento");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(estadoActual,BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public void vistaTransmitir(){
        construirUI();
        setTitle("Cliente - Conectado ");
        estadoActual.setText("Conexion exitosa con el servidor, transmitiendo eventos y pantalla de este ordenador");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(estadoActual,BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    public void vistaCancelada() {
        construirUI();
        setTitle("Cliente - Conexión Finalizada");

        estadoActual.setText("La conexión ha sido cerrada por el servidor, poniendo en espera nuevamente");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(estadoActual,BorderLayout.CENTER);

        JButton botonAceptar = new JButton("Aceptar");
        JPanel botonPanel = new JPanel();
        botonPanel.add(botonAceptar);
        panel.add(botonPanel, BorderLayout.SOUTH);
        panel.revalidate();
        panel.repaint();

        botonAceptar.addActionListener(e -> mostrarInterfazEscucha());
    }


    private void construirUI() {
        panel.removeAll();
        panel.setLayout(new GridLayout(2, 1));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);


    }
}

