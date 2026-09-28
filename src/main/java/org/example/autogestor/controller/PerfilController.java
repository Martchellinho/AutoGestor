package org.example.autogestor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.autogestor.model.Usuario;
import org.example.autogestor.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
public class PerfilController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PerfilController(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // =========================
    // MEU PERFIL
    // =========================

    @GetMapping("/perfil")
    public String perfil(
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        model.addAttribute("usuario", usuario);

        return "perfil";
    }


    // =========================
    // EDITAR PERFIL
    // =========================

    @GetMapping("/perfil/editar")
    public String editarPerfil(
            Authentication authentication,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        model.addAttribute("usuario", usuario);

        return "perfil-editar";
    }


    @PostMapping("/perfil/salvar")
    public String salvarPerfil(
            Authentication authentication,
            @RequestParam String nome,
            @RequestParam String username,
            HttpServletRequest request,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        var usuarioExistente =
                usuarioRepository.findByUsername(username);

        if (usuarioExistente.isPresent()
                && !usuarioExistente.get()
                .getId()
                .equals(usuario.getId())) {

            model.addAttribute("usuario", usuario);

            model.addAttribute(
                    "erro",
                    "Este nome de usuário já está sendo utilizado."
            );

            return "perfil-editar";
        }

        boolean mudouUsername =
                !usuario.getUsername().equals(username);

        usuario.setNome(nome);
        usuario.setUsername(username);

        usuarioRepository.save(usuario);

        if (mudouUsername) {

            SecurityContextHolder.clearContext();

            request.getSession().invalidate();

            return "redirect:/login";
        }

        return "redirect:/perfil";
    }


    // =========================
    // ALTERAR SENHA
    // =========================

    @GetMapping("/perfil/senha")
    public String alterarSenha() {

        return "perfil-senha";
    }


    @PostMapping("/perfil/senha")
    public String salvarSenha(
            Authentication authentication,
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha,
            @RequestParam String confirmarSenha,
            Model model) {

        Usuario usuario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        // Verifica senha atual
        if (!passwordEncoder.matches(
                senhaAtual,
                usuario.getSenha())) {

            model.addAttribute(
                    "erro",
                    "A senha atual está incorreta."
            );

            return "perfil-senha";
        }

        // Confere as duas novas senhas
        if (!novaSenha.equals(confirmarSenha)) {

            model.addAttribute(
                    "erro",
                    "As novas senhas não são iguais."
            );

            return "perfil-senha";
        }

        // Tamanho mínimo
        if (novaSenha.length() < 4) {

            model.addAttribute(
                    "erro",
                    "A nova senha deve possuir pelo menos 4 caracteres."
            );

            return "perfil-senha";
        }

        // Criptografa senha nova
        usuario.setSenha(
                passwordEncoder.encode(novaSenha)
        );

        usuarioRepository.save(usuario);

        model.addAttribute(
                "sucesso",
                "Senha alterada com sucesso."
        );

        return "perfil-senha";
    }


    // =========================
    // FOTO DE PERFIL
    // =========================

    @PostMapping("/perfil/foto")
    public String salvarFoto(
            Authentication authentication,
            @RequestParam("foto") MultipartFile foto)
            throws IOException {

        Usuario usuario = usuarioRepository
                .findByUsername(authentication.getName())
                .orElseThrow();

        // Se não selecionou arquivo
        if (foto.isEmpty()) {
            return "redirect:/perfil";
        }

        // Verifica se o arquivo é uma imagem
        String tipo = foto.getContentType();

        if (tipo == null || !tipo.startsWith("image/")) {
            return "redirect:/perfil";
        }

        // Descobre extensão
        String nomeOriginal = foto.getOriginalFilename();

        String extensao = ".jpg";

        if (nomeOriginal != null
                && nomeOriginal.contains(".")) {

            extensao = nomeOriginal.substring(
                    nomeOriginal.lastIndexOf(".")
            );
        }

        // Cria nome único
        String nomeArquivo =
                UUID.randomUUID().toString() + extensao;

        // Local da pasta uploads/perfil
        Path pasta = Paths.get(
                        "uploads",
                        "perfil"
                )
                .toAbsolutePath()
                .normalize();

        // Garante que a pasta exista
        Files.createDirectories(pasta);

        Path destino =
                pasta.resolve(nomeArquivo);

        // Salva a foto
        Files.copy(
                foto.getInputStream(),
                destino,
                StandardCopyOption.REPLACE_EXISTING
        );

        // Salva nome da foto no MySQL
        usuario.setFoto(nomeArquivo);

        usuarioRepository.save(usuario);

        return "redirect:/perfil";
    }
}