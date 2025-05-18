package org.proy.monitorizerdesktop.auth.views;

import org.proy.monitorizerdesktop.auth.controllers.InicioController;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@Component
public class InicioView extends JFrame {

    private final VistaFactory vistaFactory;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> rolComboBox;
    private JButton iniciarButton;
    private JLabel mensajeErrorLabel;
    private final InicioController controller;


    public InicioView(InicioController controller, VistaFactory vistaFactory) {
        this.controller = controller;
        this.vistaFactory = vistaFactory;
        SwingUtilities.invokeLater(this::buildUI);

    }

    private void buildUI() {
        setTitle("Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

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

        mensajeErrorLabel = new JLabel("");
        mensajeErrorLabel.setForeground(Color.RED);
        mensajeErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        iniciarButton = new JButton("Iniciar sesión");
        iniciarButton.addActionListener(e -> {
            String email = getEmail();
            String password = getPassword();
            String rol = getRol();
            if (email.isEmpty() || password.isEmpty() || rol == null) {
                mensajeErrorLabel.setText("Completa todos los campos.");
            } else {
                desplegarVistaRol(email, password, rol);
            }
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

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(mensajeErrorLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        panel.add(iniciarButton, gbc);

        getContentPane().add(panel);
        setVisible(true);
    }

    private Usuario iniciarSesion(String email, String password, String rol) {

        Optional<Usuario> usuario = controller.autenticarYObtenerUsuario(email, password);
        if ( usuario.isPresent()) {
           return usuario.get();
        }else{
            mensajeErrorLabel.setText("Email o contraseña incorrectas");
            throw new RuntimeException("Email o contraseña incorrectas");
        }

    }

    private void desplegarVistaRol(String email, String password,String rol) {
        Usuario usuario = iniciarSesion(email, password, rol);
       JFrame vista = vistaFactory.getVistaPorRol(rol, usuario);
       vista.setVisible(true);
       this.dispose();
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

}
