package org.example.autogestor.config;

import org.example.autogestor.model.Usuario;
import org.example.autogestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Value("${ADMIN_NAME:}")
    private String adminNome;

    @Value("${ADMIN_USERNAME:}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Bean
    CommandLineRunner criarUsuarioAdmin(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // Só cria um administrador se o banco não possuir nenhum usuário
            if (usuarioRepository.count() == 0) {

                // Só cria se as variáveis estiverem configuradas
                if (adminUsername.isBlank() || adminPassword.isBlank()) {

                    System.out.println(
                            "Nenhum usuário cadastrado. Configure ADMIN_USERNAME e ADMIN_PASSWORD."
                    );

                    return;
                }

                Usuario usuario = new Usuario();

                usuario.setNome(
                        adminNome.isBlank() ? "Administrador" : adminNome
                );

                usuario.setUsername(adminUsername);

                usuario.setSenha(
                        passwordEncoder.encode(adminPassword)
                );

                usuario.setPerfil("ADMIN");

                usuarioRepository.save(usuario);

                System.out.println(
                        "Usuário administrador inicial criado."
                );
            }
        };
    }
}