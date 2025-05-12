package org.proy.monitorizerdesktop.auth.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServidorView extends JFrame {

    private JTable tablaConexiones;
    private JLabel labelEstado;
    private JButton btnNuevaConexion;

    public ServidorView() {
        buildUI();
    }

    private void buildUI() {
        setTitle("Servidor - Gestión de Conexiones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout());


        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNuevaConexion = new JButton("Nueva conexión");
        labelEstado = new JLabel("Conexiones 0/10");
        panelSuperior.add(btnNuevaConexion);
        panelSuperior.add(labelEstado);

        String[] columnas = {"IP", "Acciones"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablaConexiones = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tablaConexiones);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        add(panelPrincipal);
        setVisible(true);
    }

    public void actualizarConexiones(int actuales, int maximas) {
        labelEstado.setText("Conexiones " + actuales + "/" + maximas);
    }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) tablaConexiones.getModel();
    }

    public JButton getBtnNuevaConexion() {
        return btnNuevaConexion;
    }
}
