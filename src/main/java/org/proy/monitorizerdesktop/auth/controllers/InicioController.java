package org.proy.monitorizerdesktop.auth.controllers;

import org.proy.monitorizerdesktop.auth.services.AuthService;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.springframework.stereotype.Component;
import org.proy.monitorizerdesktop.clientserver.services.UserService;
import java.util.Optional;

@Component
public class InicioController {
    private final AuthService authService;
    private final UserService userService;

    public InicioController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService= userService;
    }

    public Optional<Usuario> autenticarYObtenerUsuario(String email, String password) {
        if (authService.login(email, password)) {
            return userService.loadUser(email);
        }
        return Optional.empty();
    }


}