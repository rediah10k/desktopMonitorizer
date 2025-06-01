package org.proy.monitorizerdesktop.clientserver.classes.client;


import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import org.springframework.stereotype.Component;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

@Component
public class TransmisorEventos {

    private Socket socket;
    private DataOutputStream dos;
    private Thread hiloTransmision;
    public CapturadorEventos capturadorEventos;
    public volatile Boolean transmitiendo=false;

    public TransmisorEventos() {
        this.capturadorEventos = new CapturadorEventos(this);
    }

    public void enviarEvento(String evento) {
        if(transmitiendo){
            try {
                OutputStream out = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                byte[] datos = evento.getBytes();
                dos.writeInt(datos.length);
                dos.write(datos);
                dos.flush();

            } catch (IOException e) {
                System.out.println("Cliente desconectado: ");
            }
        }
    }

    public void setProperties(String host, Integer puerto) {

        try {
            socket = new Socket(host, puerto);
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error al establecer conexión para transmisión", e);
        }
        try {
            if (!GlobalScreen.isNativeHookRegistered()) {
                GlobalScreen.registerNativeHook();
            }
        } catch (NativeHookException ex) {
            System.err.println("Error al registrar hook nativo");
            ex.printStackTrace();
        }


    }






}
