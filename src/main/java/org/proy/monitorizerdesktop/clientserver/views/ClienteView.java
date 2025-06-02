package org.proy.monitorizerdesktop.clientserver.views;

import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.controllers.ClienteController;

import org.proy.monitorizerdesktop.clientserver.utils.IpPropiedades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;


@Component
public class ClienteView extends JFrame {

    JPanel panel;
    private ClienteController controller;
    private JTextArea estadoActual;

    @Autowired
    public ClienteView(ClienteController clienteController) {
        this.controller = clienteController;
        controller.suscribirseAListener(this);
        setConfigBase();

    }

    public void inicializarVentana(){
        construirUIInicial();
    }

    private void construirUIInicial() {
        panel.removeAll();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contenedorCentral = new JPanel(new GridBagLayout());
        contenedorCentral.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel etiquetaPuerto = new JLabel("Ingrese el puerto para escuchar:");
        etiquetaPuerto.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 0;
        contenedorCentral.add(etiquetaPuerto, gbc);

        JTextField campoPuerto = new JTextField(10);
        campoPuerto.setFont(new Font("Arial", Font.PLAIN, 14));
        campoPuerto.setPreferredSize(new Dimension(200, 30));
        gbc.gridy = 1;
        contenedorCentral.add(campoPuerto, gbc);

        JButton botonAceptar = new JButton("Aceptar");
        botonAceptar.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        contenedorCentral.add(botonAceptar, gbc);

        estadoActual.setText("Esperando ingreso del puerto...");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 14));
        estadoActual.setEditable(false);
        estadoActual.setOpaque(false);
        estadoActual.setBorder(null);

        JPanel estadoPanel = new JPanel(new BorderLayout());
        estadoPanel.setOpaque(false);
        estadoPanel.add(estadoActual, BorderLayout.CENTER);

        panel.add(contenedorCentral, BorderLayout.CENTER);
        panel.add(estadoPanel, BorderLayout.SOUTH);

        botonAceptar.addActionListener(e -> {
            String entrada = campoPuerto.getText().trim();
            try {
                int puerto = Integer.parseInt(entrada);
                if (puerto < 1024 || puerto > 65535) {
                    throw new NumberFormatException("Puerto fuera de rango");
                }

                controller.setPuerto(puerto);
                controller.iniciarCliente();
                vistaEspera();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Por favor ingrese un número de puerto válido entre 1024 y 65535.",
                        "Puerto inválido",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        panel.revalidate();
        panel.repaint();
    }

    private void setConfigBase(){
        estadoActual = new JTextArea();
        estadoActual.setEditable(false);
        estadoActual.setOpaque(false);
        estadoActual.setBorder(null);
        estadoActual.setFocusable(false);
        panel = new JPanel(new GridLayout(2, 1));
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);

    }

    public void setUsuario(UsuarioDTO usuario) {
        this.controller.setUsuario(usuario);
    }

    private void mostrarInterfazEscucha(){
        this.controller.iniciarCliente();
        vistaEspera();
    }

    public void vistaEspera() {
        construirUI();
        setTitle("Cliente - Esperando Conexiones");

        String ip = new IpPropiedades().obtenerIP();
        estadoActual.setText("Dirección IP: " + ip + "\nA la espera de conexiones en el puerto " + this.controller.getPuerto());
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel estadoPanel = new JPanel(new BorderLayout());
        estadoPanel.setOpaque(false);
        estadoPanel.add(estadoActual, BorderLayout.CENTER);

        JButton botonCambiarPuerto = new JButton("Cambiar puerto");
        botonCambiarPuerto.setFont(new Font("Arial", Font.PLAIN, 14));
        botonCambiarPuerto.addActionListener(e -> construirUIInicial());

        JPanel botonPanel = new JPanel();
        botonPanel.setOpaque(false);
        botonPanel.add(botonCambiarPuerto);

        panel.setLayout(new BorderLayout());
        panel.add(estadoPanel, BorderLayout.CENTER);
        panel.add(botonPanel, BorderLayout.SOUTH);

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