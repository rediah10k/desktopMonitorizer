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
    private JTable table;
    private ConexionesActualesTableModel tableModel;
    private JLabel labelEstado;
    private JButton btnNuevaConexion;
    private JButton btnCerrarConexion;
    private JButton btnMonitor;
    private JButton btnCompartirArchivo;
    private JButton btnMaxConexiones;
    private ServidorController controller;

    @Autowired
    public ServidorView(ServidorController servidorController) {
        this.controller = servidorController;
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
        buildUI();
    }

    private void buildUI() {
        setTitle("Servidor - Gestión de Conexiones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(800, 400);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel contenedorCentral = new JPanel(new GridBagLayout());
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

        JLabel etiquetaMax = new JLabel("Máximo de conexiones permitidas:");
        etiquetaMax.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridy = 2;
        contenedorCentral.add(etiquetaMax, gbc);

        JTextField campoMaxConexiones = new JTextField(10);
        campoMaxConexiones.setFont(new Font("Arial", Font.PLAIN, 14));
        campoMaxConexiones.setPreferredSize(new Dimension(200, 30));
        gbc.gridy = 3;
        contenedorCentral.add(campoMaxConexiones, gbc);

        JButton botonAceptar = new JButton("Aceptar");
        botonAceptar.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 4;
        contenedorCentral.add(botonAceptar, gbc);

        JLabel estadoActual = new JLabel("Esperando ingreso del puerto y conexiones...");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel estadoPanel = new JPanel(new BorderLayout());
        estadoPanel.add(estadoActual, BorderLayout.CENTER);

        panel.add(contenedorCentral, BorderLayout.CENTER);
        panel.add(estadoPanel, BorderLayout.SOUTH);

        botonAceptar.addActionListener(e -> {
            String entradaPuerto = campoPuerto.getText().trim();
            String entradaMax = campoMaxConexiones.getText().trim();
            try {
                int puerto = Integer.parseInt(entradaPuerto);
                int max = Integer.parseInt(entradaMax);
                if (puerto < 1024 || puerto > 65535) {
                    throw new NumberFormatException("Puerto fuera de rango");
                }
                if (max <= 0 || max > 10) {
                    throw new NumberFormatException("Conexiones inválidas");
                }

                controller.setPuerto(puerto);
                controller.establecerMaximoConexiones(max);
                construirInterfazPrincipal();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "El puerto debe estar entre 1024 y 65535.\nEl número de conexiones debe ser entre 1 y 10.",
                        "Entrada inválida",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });


        setContentPane(panel);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void construirInterfazPrincipal() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnNuevaConexion = new JButton("Nueva conexión");
        btnCerrarConexion = new JButton("Cerrar conexion");
        btnMonitor= new JButton("Observar PC");
        btnCompartirArchivo= new JButton("Enviar archivo");


        btnCerrarConexion = new JButton("Cerrar conexión");
        btnMonitor = new JButton("Observar PC");
        btnMaxConexiones = new JButton("Cambiar Max Conexiones");
        labelEstado = new JLabel("");

        panelSuperior.add(btnNuevaConexion);
        panelSuperior.add(btnCerrarConexion);
        panelSuperior.add(btnCompartirArchivo);
        panelSuperior.add(btnMonitor);
        panelSuperior.add(labelEstado);

        JScrollPane scrollPane = new JScrollPane(table);
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        btnNuevaConexion.addActionListener(e -> mostrarVentanaAgregar());
        btnCerrarConexion.addActionListener(e -> mostrarVentanaCerrar());
        btnMonitor.addActionListener(e -> {
            mostrarVentanaMonitor();
            solicitarTransmision();
        });

        btnCompartirArchivo.addActionListener(e -> {
            mostrarVentanaSeleccionArchivo();
        });

        add(panelPrincipal);

        setContentPane(panelPrincipal);
        setSize(600, 400);
        setLocationRelativeTo(null);
        actualizarTabla();
        revalidate();
        repaint();
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
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        JLabel mensaje = new JLabel("Ingresa la IP y el puerto del cliente:");
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(mensaje, BorderLayout.NORTH);

        JTextField campoIP = new JTextField(12);
        JTextField campoPuerto = new JTextField(5);

        JPanel centro = new JPanel();
        centro.add(new JLabel("IP:"));
        centro.add(campoIP);
        centro.add(new JLabel("Puerto:"));
        centro.add(campoPuerto);
        frame.add(centro, BorderLayout.CENTER);
        JPanel sur = new JPanel();
        frame.add(sur, BorderLayout.SOUTH);
        JButton conectar = new JButton("Conectar");
        sur.add(conectar);
        conectar.addActionListener(e -> {
            try {
                int puerto = Integer.parseInt(campoPuerto.getText().trim());
                String ip = campoIP.getText().trim();
                controller.agregarCliente(ip, puerto);
                actualizarTabla();
                frame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Puerto inválido. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
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
        if (row != -1) {
            ConexionDTO clienteConectado = tableModel.getClientes().get(row);
            controller.eliminarCliente(clienteConectado);

            JFrame frame = new JFrame("Cliente desconectado");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(400, 180);
            frame.setLayout(new BorderLayout(20, 20));
            frame.setResizable(false);

            JLabel mensaje = new JLabel("<html><center>La conexión con el cliente<br>ha sido cerrada exitosamente.</center></html>");
            mensaje.setHorizontalAlignment(SwingConstants.CENTER);
            mensaje.setFont(new Font("Arial", Font.PLAIN, 15));
            frame.add(mensaje, BorderLayout.CENTER);

            JPanel panelBoton = new JPanel();
            JButton botonAceptar = new JButton("Aceptar");
            botonAceptar.setPreferredSize(new Dimension(100, 30));
            botonAceptar.setFont(new Font("Arial", Font.PLAIN, 13));
            panelBoton.add(botonAceptar);
            frame.add(panelBoton, BorderLayout.SOUTH);

            botonAceptar.addActionListener(e -> frame.dispose());

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            actualizarTabla();
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
