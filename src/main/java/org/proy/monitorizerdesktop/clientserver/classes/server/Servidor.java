package org.proy.monitorizerdesktop.clientserver.classes.server;

import lombok.Getter;
import lombok.Setter;
import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class Servidor {

    UsuarioDTO usuario;
    GestorServidor listConexiones;

    public Servidor(GestorServidor listConexiones) {
        this.listConexiones = listConexiones;
    }

    public GestorServidor getGestorConexiones() {
        return listConexiones;
    }


}
