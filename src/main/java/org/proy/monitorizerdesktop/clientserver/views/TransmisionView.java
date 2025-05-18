package org.proy.monitorizerdesktop.clientserver.views;

import org.proy.monitorizerdesktop.clientserver.controllers.ServidorController;
import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;


public class TransmisionView {
    private ConexionDTO clienteVisualizado;
    private ServidorController controller;
    private JFrame monitor=new JFrame();
    private JLabel pantalla=new JLabel();
    private JButton btnSalir=new JButton("Salir");



    public TransmisionView(ConexionDTO clienteVisualizado, ServidorController controller) {
        this.clienteVisualizado = clienteVisualizado;
        this.controller = controller;
    }

    public void mostrarVentana() {
        monitor.setTitle("Escritorio Remoto");
        monitor.setSize(600, 400);
        monitor.setLocationRelativeTo(null);
        monitor.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        monitor.setLayout(new BorderLayout());
        monitor.add(pantalla, BorderLayout.CENTER);
        monitor.add(btnSalir, BorderLayout.SOUTH);

        btnSalir.addActionListener(e -> cerrarTransmision());

        monitor.setVisible(true);
    }

    public void cerrarTransmision() {
        controller.cerrarTransmision(clienteVisualizado);
        monitor.dispose();
    }

    public void actualizarImagen(BufferedImage image) {
        int ancho = pantalla.getWidth();
        int alto = pantalla.getHeight();
        pantalla.setIcon(escalarImagen(image, ancho, alto));
        pantalla.revalidate();
        pantalla.repaint();
    }

    private ImageIcon escalarImagen(BufferedImage original, int ancho, int alto) {
        Image imagenEscalada = original.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(imagenEscalada);
    }

}