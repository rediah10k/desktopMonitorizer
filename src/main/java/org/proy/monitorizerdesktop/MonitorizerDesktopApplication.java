package org.proy.monitorizerdesktop;

import org.proy.monitorizerdesktop.auth.controller.InicioController;
import org.proy.monitorizerdesktop.auth.views.InicioView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class MonitorizerDesktopApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(MonitorizerDesktopApplication.class, args);
        InicioController controller =  context.getBean(InicioController.class);
        InicioView inicioView = new InicioView(controller);
        inicioView.setVisible(true);


    }
}
