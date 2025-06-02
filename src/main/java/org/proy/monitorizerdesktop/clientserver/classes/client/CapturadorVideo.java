package org.proy.monitorizerdesktop.clientserver.classes.client;

import org.proy.monitorizerdesktop.clientserver.utils.GeneradorVideoLocal;
import org.springframework.stereotype.Component;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


@Component
public class CapturadorVideo {

    private Boolean capturando;
    private Integer fps = 60;
    private GeneradorVideoLocal videoGenerator;
    private CapturadorPantalla capturadorPantalla;

    public CapturadorVideo() {
        capturadorPantalla = new CapturadorPantalla();
        videoGenerator = new GeneradorVideoLocal();
    }

    public GeneradorVideoLocal getGeneradorVideoLocal() {
        return videoGenerator;
    }

    public void setCapturando(Boolean capturando) {
        this.capturando = capturando;
    }

    public void iniciarCaptura() {
        try{
            Dimension dimension = capturadorPantalla.getScreenSize();
            videoGenerator.setHeight(dimension.height);
            videoGenerator.setWidth(dimension.width);
            videoGenerator.setFps(fps);
            videoGenerator.setRecorderProperties();
            setCapturando(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public BufferedImage capturarVideo() {
        try {
            if(capturando){

                BufferedImage screen = capturadorPantalla.capturar();
                if(screen == null){
                    System.err.println("Frame capturado es null. Posiblemente se detuvo la captura.");
                    capturando = false;
                    return null;
                }
                videoGenerator.anadirFrame(screen);
                return screen;
            }


        } catch (Exception e) {
            System.err.println("Captura finalizada o fallida: " + e.getMessage());
        }
        return null;
    }

    public void detenerCaptura() {
        capturando = false;
        videoGenerator.detenerGeneracion();
        videoGenerator.guardarVideo("clientmedia");
    }
}