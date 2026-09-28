package org.example.autogestor.controller;

import org.example.autogestor.model.Cliente;
import org.example.autogestor.model.Veiculo;
import org.example.autogestor.repository.ClienteRepository;
import org.example.autogestor.repository.VeiculoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    public VeiculoController(
            VeiculoRepository veiculoRepository,
            ClienteRepository clienteRepository) {

        this.veiculoRepository = veiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "veiculos",
                veiculoRepository.findAll()
        );

        return "veiculos";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        model.addAttribute(
                "veiculo",
                new Veiculo()
        );

        model.addAttribute(
                "clientes",
                clienteRepository.findAll()
        );

        return "veiculo-form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Veiculo veiculo,
            @RequestParam Integer clienteId) {

        Cliente cliente = clienteRepository
                .findById(clienteId)
                .orElseThrow();

        veiculo.setCliente(cliente);

        veiculoRepository.save(veiculo);

        return "redirect:/veiculos";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model) {

        Veiculo veiculo = veiculoRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute(
                "veiculo",
                veiculo
        );

        model.addAttribute(
                "clientes",
                clienteRepository.findAll()
        );

        return "veiculo-form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(
            @PathVariable Integer id,
            Model model) {

        try {

            veiculoRepository.deleteById(id);
            veiculoRepository.flush();

            return "redirect:/veiculos";

        } catch (Exception e) {

            model.addAttribute(
                    "veiculos",
                    veiculoRepository.findAll()
            );

            model.addAttribute(
                    "erro",
                    "Não é possível excluir este veículo porque existem registros vinculados a ele."
            );

            return "veiculos";
        }
    }
}