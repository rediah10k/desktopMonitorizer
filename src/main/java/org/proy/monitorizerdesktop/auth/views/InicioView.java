package org.proy.monitorizerdesktop.auth.views;

import org.proy.monitorizerdesktop.auth.controller.InicioController;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

public class InicioView extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> rolComboBox;
    private JButton iniciarButton;
    private final InicioController controller;

    public InicioView(InicioController controller) {
        this.controller = controller;
        SwingUtilities.invokeLater(this::buildUI);

    }

    private void buildUI() {
        setTitle("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null); // Centrar ventana

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();

        JLabel rolLabel = new JLabel("Rol:");
        rolComboBox = new JComboBox<>(new String[]{"Cliente", "Servidor"});
        rolComboBox.setSelectedIndex(-1);

        iniciarButton = new JButton("Iniciar sesión");
        iniciarButton.addActionListener(e -> {
            String email = getEmail();
            String password = getPassword();
            String rol = getRol();
            desplegarVistaRol(email,  password, rol);
        });

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(rolLabel, gbc);
        gbc.gridx = 1;
        panel.add(rolComboBox, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        panel.add(iniciarButton, gbc);

        getContentPane().add(panel);
        setVisible(true);
    }

    private void desplegarVistaRol(String email, String password, String rol) {
        JFrame vista=null;
        Boolean validado= controller.validarUsuario(email, password);
        if (validado) {
            if ("Cliente".equalsIgnoreCase(rol)) {
                vista = new ClienteView();
            } else if ("Servidor".equalsIgnoreCase(rol)) {
                vista = new ServidorView();
            }
        }


    }

    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getRol() {
        return (String) rolComboBox.getSelectedItem();
    }

    public JButton getIniciarButton() {
        return iniciarButton;
    }
}
