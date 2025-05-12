package org.proy.monitorizerdesktop.main.views;

import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.main.classes.Servidor;
import org.proy.monitorizerdesktop.main.controllers.ServidorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Component
public class ServidorView extends JFrame {
    private JTable tablaConexiones;
    private JLabel labelEstado;
    private JButton btnNuevaConexion;
    private ServidorController servidorController;

    @Autowired
    public ServidorView(ServidorController servidorController) {
        this.servidorController = servidorController;
        buildUI();
    }

    public void setUsuario(Usuario usuario) {
        this.servidorController.setUsuario(usuario);

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
