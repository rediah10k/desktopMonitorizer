package org.proy.monitorizerdesktop.auth.controller;

import org.proy.monitorizerdesktop.auth.repos.UsuarioRepository;
import org.proy.monitorizerdesktop.auth.services.AuthService;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class InicioController {
    private final AuthService authService;

    public InicioController(AuthService authService) {
        this.authService = authService;
    }

    public Boolean validarUsuario(String email, String password) {
        Boolean validado=authService.login(email, password);
       return validado;
    }

    public Usuario getUsuario() {
        
        return null;
    }
}