package org.proy.monitorizerdesktop.clientserver.views;

import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.clientserver.controllers.ClienteController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Component
public class ClienteView extends JFrame {

    JPanel panel;
    private ClienteController clienteController;
    private JLabel estadoActual;
    private PuertoView puertoView;

    @Autowired
    public ClienteView(ClienteController clienteController, PuertoView puertoView) {
        this.clienteController = clienteController;
        this.puertoView = puertoView;
        setConfigBase();

    }

    public void inicializarVentana(){
        puertoView.pedirPuerto();
        mostrarInterfazEscucha();

    }

    private void setConfigBase(){
        estadoActual = new JLabel("",SwingConstants.CENTER);
        panel = new JPanel(new GridLayout(2, 1));
        setSize(590, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(panel);
        puertoView.setParentComponents(this, clienteController);

    }



    public void setUsuario(UsuarioDTO usuario) {
        this.clienteController.setUsuario(usuario);
    }


    private void mostrarInterfazEscucha(){
        this.clienteController.iniciarEscucha();
        vistaEspera();
    }

    private void obtenerIP(){

                try {
                    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

                    while (interfaces.hasMoreElements()) {
                        NetworkInterface ni = interfaces.nextElement();


                        if (!ni.isUp() || ni.isLoopback()) continue;

                        // En Windows, la interfaz Wi-Fi suele contener "wlan" o "wi-fi" en su nombre
                        String displayName = ni.getDisplayName().toLowerCase();
                        if (!displayName.contains("wlan") && !displayName.contains("wi-fi")) continue;

                        Enumeration<InetAddress> addresses = ni.getInetAddresses();
                        while (addresses.hasMoreElements()) {
                            InetAddress addr = addresses.nextElement();

                            // Solo direcciones IPv4 que no sean loopback
                            if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                                System.out.println("Interfaz inalámbrica encontrada: " + ni.getDisplayName());
                                System.out.println("Dirección IP LAN inalámbrica: " + addr.getHostAddress());
                                return;
                            }
                        }
                    }

                    System.out.println("No se encontró una interfaz inalámbrica activa.");
                } catch (SocketException e) {
                    e.printStackTrace();
                }

        }



    public void vistaEspera(){
        construirUI();
        setTitle("Cliente - Esperando Conexiones");

        obtenerIP();

        estadoActual.setText("A la espera de conexiones en el puerto " + this.clienteController.getPuerto());
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));


        panel.add(estadoActual);

        panel.revalidate();
        panel.repaint();
    }

    public void vistaConectada(){
        construirUI();
        setTitle("Cliente - Conectado ");
        estadoActual.setText("Conexion exitosa con el servidor, sin transmitir en este momento");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(estadoActual);
        panel.revalidate();
        panel.repaint();
    }

    public void vistaTransmitir(){
        construirUI();
        setTitle("Cliente - Conectado ");
        estadoActual.setText("Conexion exitosa con el servidor, transmitiendo eventos y pantalla de este ordenador");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(estadoActual);
        panel.revalidate();
        panel.repaint();
    }

    public void vistaCancelada() {
        construirUI();
        setTitle("Cliente - Conexión Finalizada");

        estadoActual.setText("La conexión ha sido cerrada por el servidor, poniendo en espera nuevamente");
        estadoActual.setFont(new Font("Arial", Font.PLAIN, 16));
        panel.add(estadoActual);

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

