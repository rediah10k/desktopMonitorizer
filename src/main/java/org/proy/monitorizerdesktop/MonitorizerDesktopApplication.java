package org.proy.monitorizerdesktop;

import org.proy.monitorizerdesktop.auth.controllers.InicioController;
import org.proy.monitorizerdesktop.auth.views.InicioView;
import org.proy.monitorizerdesktop.main.classes.Cliente;
import org.proy.monitorizerdesktop.main.classes.Servidor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({Cliente.class, Servidor.class})
public class MonitorizerDesktopApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(MonitorizerDesktopApplication.class, args);
        InicioView inicioView = context.getBean(InicioView.class);
        inicioView.setVisible(true);


    }
}
