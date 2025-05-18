package org.proy.monitorizerdesktop.clientserver.classes.interfaces;

import org.proy.monitorizerdesktop.clientserver.dtos.UsuarioDTO;

public interface IController {
    public Integer getPuerto();
    public void setPuerto(Integer puerto);
    public UsuarioDTO getUsuario();
    public void setUsuario(UsuarioDTO usuario);
}
