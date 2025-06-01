package org.proy.monitorizerdesktop.clientserver.classes.client;

import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

class CapturadorEventos implements NativeKeyListener, NativeMouseInputListener {

    private EventosListener eventosListener;

    public CapturadorEventos(TransmisorEventos transmisorEventos) {
        this.eventosListener = new EventosListener(transmisorEventos);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        String mensaje= "Tecla presionada: " + NativeKeyEvent.getKeyText(e.getKeyCode());
        eventosListener.onEventoCapturado(mensaje);
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent e) {
        String mensaje =  "Mouse clic en: " + e.getX() + ", " + e.getY();
        eventosListener.onEventoCapturado(mensaje);
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent e) {
        String mensaje = "Mouse movido a: " + e.getX() + ", " + e.getY();
        eventosListener.onEventoCapturado(mensaje);
    }


}
