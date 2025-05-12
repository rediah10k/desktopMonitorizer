package org.proy.monitorizerdesktop.main.classes;

import lombok.Getter;
import lombok.Setter;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "cliente")
@Getter
@Setter
public class Cliente {
    Usuario usuario;
    Integer puerto;

    public Cliente() {
        puerto = 0;
    }

}
