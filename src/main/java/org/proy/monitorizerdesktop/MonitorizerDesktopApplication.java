package org.proy.monitorizerdesktop;

import org.proy.monitorizerdesktop.auth.views.InicioView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(App.class, args);
        SwingUtilities.invokeLater(() -> {
            InicioView inicioView = ctx.getBean(InicioView.class);
            inicioView.setVisible(true);
        });
    }
}
