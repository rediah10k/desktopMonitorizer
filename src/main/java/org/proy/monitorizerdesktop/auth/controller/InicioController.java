package org.proy.monitorizerdesktop.auth.controller;

import org.springframework.stereotype.Component;

@Component
public class InicioController {
    public Boolean validarUsuario(String email, String password) {
        System.out.println("Validando usuario");
        return true;
    }
}