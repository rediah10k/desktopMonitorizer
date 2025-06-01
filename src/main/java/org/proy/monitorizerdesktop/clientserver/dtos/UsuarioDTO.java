package org.proy.monitorizerdesktop.clientserver.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {

    private String email;
    private Long Id;

    public UsuarioDTO(String email, Long Id) {
        this.email = email;
        this.Id = Id;
    }
}
