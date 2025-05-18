package org.proy.monitorizerdesktop.clientserver.controllers;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.IController;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.classes.client.Cliente;
import org.proy.monitorizerdesktop.clientserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteController implements IController {
    private final Cliente cliente;
    private final UserService userService;

    @Autowired
    public ClienteController(UserService userService, Cliente cliente) {
        this.userService = userService;
        this.cliente = cliente;
    }

   public void setUsuario(UsuarioDTO usuario) {
        this.cliente.setUsuario(usuario);

   }


   public Integer getPuerto(){
        return cliente.getGestorCliente().getPuerto();
   }

   public void setPuerto(Integer puerto){
        this.cliente.getGestorCliente().setPuerto(puerto);
   }

    @Override
    public UsuarioDTO getUsuario() {
        return this.cliente.getUsuario();
    }

    public void iniciarEscucha() {
       this.cliente.iniciarEscucha();
   }







}
