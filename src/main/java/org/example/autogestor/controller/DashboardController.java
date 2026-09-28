package org.example.autogestor.controller;

import org.example.autogestor.repository.ClienteRepository;
import org.example.autogestor.repository.VeiculoRepository;
import org.example.autogestor.repository.ServicoRepository;
import org.example.autogestor.repository.OrdemServicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final ClienteRepository clienteRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    public DashboardController(
            ClienteRepository clienteRepository,
            VeiculoRepository veiculoRepository,
            ServicoRepository servicoRepository,
            OrdemServicoRepository ordemServicoRepository) {

        this.clienteRepository = clienteRepository;
        this.veiculoRepository = veiculoRepository;
        this.servicoRepository = servicoRepository;
        this.ordemServicoRepository = ordemServicoRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {

        model.addAttribute(
                "totalClientes",
                clienteRepository.count()
        );

        model.addAttribute(
                "totalVeiculos",
                veiculoRepository.count()
        );

        model.addAttribute(
                "totalServicos",
                servicoRepository.count()
        );

        model.addAttribute(
                "totalOrdens",
                ordemServicoRepository.count()
        );

        return "index";
    }
}