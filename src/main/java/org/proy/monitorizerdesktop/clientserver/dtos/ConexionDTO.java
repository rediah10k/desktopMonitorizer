package org.proy.monitorizerdesktop.clientserver.dtos;


import lombok.Getter;

@Getter
public class ConexionDTO {
    private String ip;
    private Integer puerto;

    public ConexionDTO(String ip, Integer puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }
}
