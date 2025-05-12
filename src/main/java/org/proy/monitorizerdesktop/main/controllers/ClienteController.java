package org.proy.monitorizerdesktop.main.controllers;

import org.proy.monitorizerdesktop.entities.Usuario;
import org.proy.monitorizerdesktop.main.classes.Cliente;
import org.proy.monitorizerdesktop.main.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClienteController {
    private final Cliente cliente;
    private final UserService userService;

    @Autowired
    public ClienteController(UserService userService, Cliente cliente) {
        this.userService = userService;
        this.cliente = cliente;
    }

   public void setUsuario(Usuario usuario) {
        this.cliente.setUsuario(usuario);
   }



}
