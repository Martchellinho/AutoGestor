package org.example.autogestor.controller;

import org.example.autogestor.model.Usuario;
import org.example.autogestor.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UsuarioGlobalController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioGlobalController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @ModelAttribute("usuarioLogado")
    public Usuario usuarioLogado(Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getName().equals("anonymousUser")) {

            return null;
        }

        return usuarioRepository
                .findByUsername(authentication.getName())
                .orElse(null);
    }
}