package org.proy.monitorizerdesktop.main.classes;

import lombok.Getter;
import lombok.Setter;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;


@Component
@ConfigurationProperties(prefix = "servidor")
@Getter
@Setter
public class Servidor {
    Usuario usuario;
    Integer maxConexiones;
    ExecutorService pool;



}
