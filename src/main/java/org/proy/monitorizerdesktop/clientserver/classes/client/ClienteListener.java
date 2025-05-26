package org.proy.monitorizerdesktop.clientserver.classes.client;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.ConexionListener;
import org.proy.monitorizerdesktop.clientserver.views.ClienteView;


public class ClienteListener implements ConexionListener {
    private ClienteView clienteView;

    public ClienteListener(ClienteView clienteView) {
        this.clienteView = clienteView;
    }

    public void onConexionAceptada() {
        clienteView.vistaConectada();
    }

    public void onTransmision() {
        clienteView.vistaTransmitir();

    }

    public void onConexionCerrada() {
        clienteView.vistaCancelada();
    }


}
