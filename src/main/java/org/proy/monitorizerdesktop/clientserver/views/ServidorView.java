package org.proy.monitorizerdesktop.clientserver.views;

import org.proy.monitorizerdesktop.clientserver.dtos.ConexionDTO;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.views.tablemodels.ConexionesActualesTableModel;
import org.proy.monitorizerdesktop.clientserver.controllers.ServidorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

@Component
public class ServidorView extends JFrame {
    TransmisionView transmisionView;
    PuertoView puertoView;
    private JTable table;
    private ConexionesActualesTableModel tableModel;
    private JLabel labelEstado;
    private JButton btnNuevaConexion;
    private JButton btnCerrarConexion;
    private JButton btnMonitor;
    private JButton btnCompartirArchivo;
    private ServidorController controller;

    @Autowired
    public ServidorView(ServidorController servidorController, PuertoView puertoView) {
        this.controller = servidorController;
        this.puertoView = puertoView;
        tableModel = new ConexionesActualesTableModel(List.of());
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        controller.suscribirseAListener(this);
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.controller.setUsuario(usuario);

    }

    public TransmisionView getTransmisionView() {
        return transmisionView;
    }

    public void inicializarVentana(){
        this.puertoView.setParentComponents(this,controller);
        puertoView.pedirPuerto();
        buildUI();
    }

    private void buildUI() {
        setTitle("Servidor - Gestión de Conexiones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnNuevaConexion = new JButton("Nueva conexión");
        btnCerrarConexion = new JButton("Cerrar conexion");
        btnMonitor= new JButton("Observar PC");
        btnCompartirArchivo= new JButton("Enviar archivo");


        labelEstado = new JLabel("");
        panelSuperior.add(btnNuevaConexion);
        panelSuperior.add(btnCerrarConexion);
        panelSuperior.add(btnCompartirArchivo);
        panelSuperior.add(btnMonitor);
        panelSuperior.add(labelEstado);

        JScrollPane scrollPane = new JScrollPane(table);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        btnNuevaConexion.addActionListener(e -> {
            mostrarVentanaAgregar();
        });

        btnCerrarConexion.addActionListener(e -> {
            mostrarVentanaCerrar();
        });

        btnMonitor.addActionListener(e -> {

            mostrarVentanaMonitor();
            solicitarTransmision();
        });

        btnCompartirArchivo.addActionListener(e -> {
            mostrarVentanaSeleccionArchivo();
        });

        add(panelPrincipal);
        actualizarTabla();
    }

    public void solicitarTransmision() {
        int row = table.getSelectedRow();
        if(row != -1) {
            ConexionDTO clienteConectado = tableModel.getClientes().get(row);
            controller.solicitarTransmision(clienteConectado);
        }
    }

    public void actualizarImagen(BufferedImage imagen) {
        transmisionView.actualizarImagen(imagen);
    }

    public void mostrarVentanaMonitor(){
        int row = table.getSelectedRow();
        if(row != -1) {
            ConexionDTO clienteConectado = tableModel.getClientes().get(row);
            transmisionView = new TransmisionView(clienteConectado,controller);
            transmisionView.mostrarVentana();
        }
    }

    public void mostrarVentanaAgregar() {
        JFrame frame = new JFrame("Agregar cliente");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(350, 200);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel mensaje = new JLabel("Ingresa la dirección IP local del ordenador y el puerto:");
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(mensaje, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(2, 2, 5, 5));
        JLabel labelIP = new JLabel("Dirección IP:");
        JTextField campoIP = new JTextField(15);
        JLabel labelPuerto = new JLabel("Puerto:");
        JTextField campoPuerto = new JTextField(5);

        centro.add(labelIP);
        centro.add(campoIP);
        centro.add(labelPuerto);
        centro.add(campoPuerto);
        frame.add(centro, BorderLayout.CENTER);
        JPanel sur = new JPanel();
        JButton botonConectar = new JButton("Conectar");
        sur.add(botonConectar);
        frame.add(sur, BorderLayout.SOUTH);

        botonConectar.addActionListener(e -> {
            String ip = campoIP.getText().trim();
            String puertoStr = campoPuerto.getText().trim();
            try {
                int puerto = Integer.parseInt(puertoStr);
                System.out.println("IP: " + ip + ", Puerto: " + puerto);
                this.controller.agregarCliente(ip, puerto);
                actualizarTabla();
                frame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "El puerto debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void mostrarVentanaSeleccionArchivo() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            controller.enviarArchivoAClientes(chooser.getSelectedFile());
        }
    }

    public void mostrarVentanaCerrar() {
        int row = table.getSelectedRow();
        if(row != -1) {
            ConexionDTO clienteConectado = tableModel.getClientes().get(row);
            controller.eliminarCliente(clienteConectado);

            JFrame frame = new JFrame("Cerrar cliente");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(350, 200);
            frame.setLayout(new BorderLayout(10, 10));

            JLabel mensaje = new JLabel("La conexion con el cliente ha sido cerrada exitosamente");
            mensaje.setHorizontalAlignment(SwingConstants.CENTER);
            frame.add(mensaje, BorderLayout.NORTH);
            JButton botonAceptar = new JButton("Aceptar");
            botonAceptar.addActionListener(e -> {
                frame.dispose();
            });
            actualizarTabla();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }


    public void actualizarCantidad(int actuales, int maximas) {
        labelEstado.setText("Conexiones " + actuales + "/" + maximas);
    }

    public void actualizarTabla() {
        List<ConexionDTO> clientesActuales=controller.obtenerUsuariosConectados();
        tableModel.setClientes(clientesActuales);
        actualizarCantidad(clientesActuales.size(), controller.getMaximasConexiones());
        tableModel.fireTableDataChanged();
    }
}
