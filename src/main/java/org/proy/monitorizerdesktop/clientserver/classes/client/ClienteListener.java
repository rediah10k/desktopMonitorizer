package org.proy.monitorizerdesktop.clientserver.classes.client;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.ConexionListener;
import org.proy.monitorizerdesktop.clientserver.views.ClienteView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ClienteListener implements ConexionListener {
    private ClienteView clienteView;

    @Autowired
    public ClienteListener(@Lazy ClienteView clienteView) {
        this.clienteView = clienteView;
    }



    public void onConexionAceptada() {
        clienteView.vistaConectada();

    }


    public void onConexionCerrada() {
        clienteView.vistaCancelada();
    }


    public void onTransmision() {
        clienteView.vistaTransmitir();

    }


}
