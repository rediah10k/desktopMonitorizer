package org.proy.monitorizerdesktop.clientserver.services;

import org.proy.monitorizerdesktop.auth.repos.UsuarioRepository;
import org.proy.monitorizerdesktop.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UserService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> loadUser(String email) {

        return usuarioRepository.findByEmail(email);
    }


}
