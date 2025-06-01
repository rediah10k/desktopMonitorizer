package org.proy.monitorizerdesktop.clientserver.controllers;

import org.proy.monitorizerdesktop.clientserver.classes.interfaces.IController;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.proy.monitorizerdesktop.clientserver.classes.client.Cliente;
import org.proy.monitorizerdesktop.clientserver.services.UserService;
import org.proy.monitorizerdesktop.clientserver.views.ClienteView;
import org.proy.monitorizerdesktop.clientserver.views.ServidorView;
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


    @Override
    public UsuarioDTO getUsuario() {
        return this.cliente.getUsuario();
    }


    public void suscribirseAListener(ClienteView view) {
        cliente.getGestorCliente().setClienteListener(view);
    }

   public Integer getPuerto(){
        return cliente.getGestorCliente().getPuerto();
   }

   public void setPuerto(Integer puerto){
        this.cliente.getGestorCliente().setPuerto(puerto);
   }


   public void iniciarCliente() {
       this.cliente.iniciarEscucha();
   }







}
