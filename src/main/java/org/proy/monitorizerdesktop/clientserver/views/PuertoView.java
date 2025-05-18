package org.proy.monitorizerdesktop.clientserver.views;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.IController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.swing.*;


@Component
public class PuertoView {
    JFrame parentFrame;
    IController controller;

    public void setParentComponents(JFrame parentFrame, IController controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;
    }


    public void pedirPuerto() {
        while (true) {
            String entrada = JOptionPane.showInputDialog(
                    this.parentFrame,
                    "Ingrese el puerto para escuchar:",
                    "Configuración de Puerto",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (entrada == null) {

                System.exit(0);
                return;
            }

            try {
                int puerto = Integer.parseInt(entrada);
                if (puerto < 1024 || puerto > 65535) {
                    throw new NumberFormatException("Puerto fuera de rango");
                }
                controller.setPuerto(puerto);
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        this.parentFrame,
                        "Por favor ingrese un número de puerto válido entre 1024 y 65535.",
                        "Puerto inválido",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }


}
