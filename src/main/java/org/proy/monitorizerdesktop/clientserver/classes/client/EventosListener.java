package org.proy.monitorizerdesktop.clientserver.classes.client;

public class EventosListener {
    private final TransmisorEventos transmisorEventos;

    public EventosListener(TransmisorEventos transmisorEventos) {
        this.transmisorEventos = transmisorEventos;
    }

    public void onEventoCapturado(String mensaje){
        transmisorEventos.enviarEvento(mensaje);
    }
}
