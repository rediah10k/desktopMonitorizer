package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.ConexionListener;
import org.proy.monitorizerdesktop.clientserver.views.ServidorView;


import java.awt.image.BufferedImage;


public class ServidorListener implements ConexionListener {
    private ServidorView servidorView;


   public ServidorListener(ServidorView servidorView) {
       this.servidorView = servidorView;
   }

    public void onTransmision(BufferedImage image) {
        servidorView.actualizarImagen(image);
    }

    public void onTransmisionCerrada(){
        servidorView.getTransmisionView().cerrarVentanaTransmision();
    }

    @Override
    public void onConexionAceptada() {
        servidorView.actualizarTabla();
    }

    @Override
    public void onConexionCerrada() {
        servidorView.actualizarTabla();
    }
}
