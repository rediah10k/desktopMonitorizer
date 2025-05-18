package org.proy.monitorizerdesktop.clientserver.classes.server;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.ConexionListener;
import org.proy.monitorizerdesktop.clientserver.views.ServidorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ServidorListener implements ConexionListener {
    private ServidorView servidorView;

    @Autowired
    public ServidorListener(@Lazy ServidorView servidorView) {

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
