package org.proy.monitorizerdesktop;

import org.proy.monitorizerdesktop.auth.views.InicioView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MonitorizerDesktopApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(MonitorizerDesktopApplication.class, args);
        InicioView inicioView = context.getBean(InicioView.class);
        inicioView.setVisible(true);


    }
}
