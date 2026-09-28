package org.example.autogestor.controller;

import org.example.autogestor.model.Cliente;
import org.example.autogestor.repository.ClienteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        return "clientes";
    }

    // ABRIR CADASTRO
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-form";
    }

    // SALVAR / ATUALIZAR
    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute("cliente") Cliente cliente) {

        clienteRepository.save(cliente);

        return "redirect:/clientes";
    }

    // EDITAR
    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable("id") Integer id,
            Model model) {

        Cliente cliente = clienteRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("cliente", cliente);

        return "cliente-form";
    }

    // EXCLUIR
    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable("id") Integer id,
            Model model) {

        try {
            clienteRepository.deleteById(id);
            clienteRepository.flush();

            return "redirect:/clientes";

        } catch (Exception e) {

            model.addAttribute(
                    "clientes",
                    clienteRepository.findAll()
            );

            model.addAttribute(
                    "erro",
                    "Não é possível excluir este cliente porque existem registros vinculados a ele."
            );

            return "clientes";
        }
    }
}