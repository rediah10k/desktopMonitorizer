package org.proy.monitorizerdesktop.clientserver.dtos;

public class UsuarioDTO {

    public String email;
    public Long Id;

    public UsuarioDTO(String email, Long Id) {
        this.email = email;
        this.Id = Id;
    }
}
