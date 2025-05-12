package org.proy.monitorizerdesktop.main.controllers;

import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.main.classes.Servidor;
import org.proy.monitorizerdesktop.main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServidorController {

    private final Servidor servidor;
    private final UserService userService;

    @Autowired
    public ServidorController(Servidor servidor, UserService userService) {
        this.servidor = servidor;
        this.userService = userService;
    }

    public void setUsuario(Usuario usuario) {
        this.servidor.setUsuario(usuario);
    }


}
