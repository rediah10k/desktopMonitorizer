package org.proy.monitorizerdesktop.main.controllers;

import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.main.services.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClienteController {

    private final UserService userService;

    public ClienteController(UserService userService) {
        this.userService = userService;
    }



}
