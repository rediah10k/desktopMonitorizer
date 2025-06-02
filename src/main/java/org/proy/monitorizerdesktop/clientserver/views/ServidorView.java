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

        JButton botonAceptar = new JButton("Aceptar");
        botonAceptar.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 2;
        contenedorCentral.add(botonAceptar, gbc);

        JLabel estadoActual = new JLabel("Esperando ingreso del puerto...");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 14));
        JPanel estadoPanel = new JPanel(new BorderLayout());
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
                construirInterfazPrincipal();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Por favor ingrese un número de puerto válido entre 1024 y 65535.",
                        "Puerto inválido",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        setContentPane(panel);
        setSize(600, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void construirInterfazPrincipal() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnNuevaConexion = new JButton("Nueva conexión");
        btnCerrarConexion = new JButton("Cerrar conexión");
        btnMonitor = new JButton("Observar PC");

        labelEstado = new JLabel("");

        panelSuperior.add(btnNuevaConexion);
        panelSuperior.add(btnCerrarConexion);
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

        setContentPane(panelPrincipal);
        setSize(600, 300);
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
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 220);
        frame.setLayout(new BorderLayout(15, 15));
        frame.setResizable(false);

        JLabel mensaje = new JLabel("Ingresa la dirección IP local del ordenador y el puerto:");
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        mensaje.setFont(new Font("Arial", Font.BOLD, 14));
        frame.add(mensaje, BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel labelIP = new JLabel("Dirección IP:");
        JTextField campoIP = new JTextField();
        campoIP.setPreferredSize(new Dimension(150, 25));

        JLabel labelPuerto = new JLabel("Puerto:");
        JTextField campoPuerto = new JTextField();
        campoPuerto.setPreferredSize(new Dimension(100, 25));

        centro.add(labelIP, gbc);
        gbc.gridx = 1;
        centro.add(campoIP, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centro.add(labelPuerto, gbc);
        gbc.gridx = 1;
        centro.add(campoPuerto, gbc);

        frame.add(centro, BorderLayout.CENTER);

        JPanel sur = new JPanel();
        JButton botonConectar = new JButton("Conectar");
        botonConectar.setPreferredSize(new Dimension(100, 30));
        sur.add(botonConectar);
        frame.add(sur, BorderLayout.SOUTH);

        botonConectar.addActionListener(e -> {
            String ip = campoIP.getText().trim();
            String puertoStr = campoPuerto.getText().trim();
            try {
                int puerto = Integer.parseInt(puertoStr);
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
