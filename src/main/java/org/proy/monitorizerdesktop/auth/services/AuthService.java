package org.proy.monitorizerdesktop.auth.services;

import org.proy.monitorizerdesktop.auth.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public boolean login(String email, String rawPassword) {
        return usuarioRepository.findByEmail(email)
                .map(usuario -> passwordEncoder.matches(rawPassword, usuario.getPassword()))
                .orElse(false);
    }
}
