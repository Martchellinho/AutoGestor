package org.example.autogestor.controller;

import org.example.autogestor.model.Usuario;
import org.example.autogestor.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================
    // LISTAR USUÁRIOS
    // =========================

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "usuarios",
                usuarioRepository.findAll()
        );

        return "usuarios";
    }


    // =========================
    // NOVO USUÁRIO
    // =========================

    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute(
                "usuario",
                new Usuario()
        );

        return "usuario-form";
    }


    // =========================
    // SALVAR USUÁRIO
    // =========================

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Usuario usuario,
            @RequestParam(required = false) String novaSenha,
            Authentication authentication,
            Model model) {

        var existenteUsername =
                usuarioRepository.findByUsername(
                        usuario.getUsername()
                );

        // Verifica username duplicado
        if (existenteUsername.isPresent()
                && (usuario.getId() == null
                || !existenteUsername.get()
                .getId()
                .equals(usuario.getId()))) {

            model.addAttribute(
                    "erro",
                    "Este nome de usuário já está sendo utilizado."
            );

            model.addAttribute(
                    "usuario",
                    usuario
            );

            return "usuario-form";
        }


        // =========================
        // NOVO USUÁRIO
        // =========================

        if (usuario.getId() == null) {

            if (novaSenha == null
                    || novaSenha.isBlank()) {

                model.addAttribute(
                        "erro",
                        "Informe uma senha para o novo usuário."
                );

                model.addAttribute(
                        "usuario",
                        usuario
                );

                return "usuario-form";
            }

            usuario.setSenha(
                    passwordEncoder.encode(novaSenha)
            );

            usuario.setFoto(null);
        }


        // =========================
        // EDITAR USUÁRIO
        // =========================

        else {

            Usuario usuarioBanco =
                    usuarioRepository
                            .findById(usuario.getId())
                            .orElseThrow();

            // A própria conta deve ser alterada pelo Meu Perfil
            if (usuarioBanco.getUsername()
                    .equals(authentication.getName())) {

                return "redirect:/perfil/editar";
            }

            // Impede remover o último administrador
            if ("ADMIN".equals(usuarioBanco.getPerfil())
                    && !"ADMIN".equals(usuario.getPerfil())
                    && usuarioRepository.countByPerfil("ADMIN") <= 1) {

                model.addAttribute(
                        "erro",
                        "O sistema precisa possuir pelo menos um administrador."
                );

                model.addAttribute(
                        "usuario",
                        usuarioBanco
                );

                return "usuario-form";
            }

            // Mantém senha atual
            usuario.setSenha(
                    usuarioBanco.getSenha()
            );

            // Mantém foto atual
            usuario.setFoto(
                    usuarioBanco.getFoto()
            );

            // Troca senha somente se informar uma nova
            if (novaSenha != null
                    && !novaSenha.isBlank()) {

                usuario.setSenha(
                        passwordEncoder.encode(
                                novaSenha
                        )
                );
            }
        }


        usuarioRepository.save(usuario);

        return "redirect:/usuarios";
    }


    // =========================
    // EDITAR USUÁRIO
    // =========================

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Authentication authentication,
            Model model) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow();

        // Se tentar editar a própria conta,
        // manda para Meu Perfil
        if (usuario.getUsername()
                .equals(authentication.getName())) {

            return "redirect:/perfil/editar";
        }

        model.addAttribute(
                "usuario",
                usuario
        );

        return "usuario-form";
    }


    // =========================
    // EXCLUIR USUÁRIO
    // =========================

    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Integer id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow();


        // Não pode excluir a própria conta
        if (usuario.getUsername()
                .equals(authentication.getName())) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Você não pode excluir sua própria conta."
            );

            return "redirect:/usuarios";
        }


        // Não pode excluir o último administrador
        if ("ADMIN".equals(usuario.getPerfil())
                && usuarioRepository.countByPerfil("ADMIN") <= 1) {

            redirectAttributes.addFlashAttribute(
                    "erro",
                    "Não é possível excluir o último administrador do sistema."
            );

            return "redirect:/usuarios";
        }


        usuarioRepository.delete(usuario);

        return "redirect:/usuarios";
    }
}