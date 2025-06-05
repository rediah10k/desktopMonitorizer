package org.proy.monitorizerdesktop.auth.controllers;

import org.proy.monitorizerdesktop.auth.services.AuthService;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
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

    public UsuarioDTO autenticarYObtenerUsuario(String email, String password) {
        if (authService.login(email, password)) {

            Optional<Usuario> user= userService.loadUser(email);
            if (user.isPresent()) {

                return new UsuarioDTO(user.get().getEmail(),user.get().getUid());
            }
        }
       return null;
    }


}