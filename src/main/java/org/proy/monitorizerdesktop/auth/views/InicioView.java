package org.proy.monitorizerdesktop.auth.views;

import org.proy.monitorizerdesktop.auth.controllers.InicioController;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
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
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Inicie sesión para comenzar",
                0, 0, new Font("SansSerif", Font.BOLD, 14)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Contraseña:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = ++row;
        panel.add(new JLabel("Rol:"), gbc);
        rolComboBox = new JComboBox<>(new String[]{"Cliente", "Servidor"});
        gbc.gridx = 1;
        panel.add(rolComboBox, gbc);

        mensajeErrorLabel = new JLabel("");
        mensajeErrorLabel.setForeground(Color.RED);
        mensajeErrorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = ++row; gbc.gridwidth = 2;
        panel.add(mensajeErrorLabel, gbc);

        iniciarButton = new JButton("Iniciar sesión");
        iniciarButton.setPreferredSize(new Dimension(150, 30));
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

        gbc.gridy = ++row;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(iniciarButton, gbc);

        add(panel);
        setVisible(true);
    }

    private UsuarioDTO iniciarSesion(String email, String password) {

        UsuarioDTO usuario = controller.autenticarYObtenerUsuario(email, password);
        if(usuario == null) {
            mensajeErrorLabel.setText("Email o contraseña incorrectas");
            throw new RuntimeException("Email o contraseña incorrectas");
        }else{
            return usuario;
        }

    }

    private void desplegarVistaRol(String email, String password,String rol) {
        UsuarioDTO usuario = iniciarSesion(email, password);
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
