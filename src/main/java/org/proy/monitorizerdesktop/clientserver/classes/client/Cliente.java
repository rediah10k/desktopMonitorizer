package org.proy.monitorizerdesktop.clientserver.classes.client;

import lombok.Getter;
import lombok.Setter;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Cliente  {
    private UsuarioDTO usuario;
    private GestorCliente gestorCliente;


    public Cliente(GestorCliente gestorCliente ) {
        this.gestorCliente = gestorCliente;
    }


    public void iniciarEscucha() {
      gestorCliente.iniciarSocketServer();
    }


}
