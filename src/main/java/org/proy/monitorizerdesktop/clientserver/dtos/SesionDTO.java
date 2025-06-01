package org.proy.monitorizerdesktop.clientserver.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SesionDTO {
    private Long clienteId;
    private Long servidorId;

    public SesionDTO(Long clienteId, Long servidorId) {
        this.clienteId = clienteId;
        this.servidorId = servidorId;
    }
}
