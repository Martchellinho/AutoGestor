package org.example.autogestor.controller;

import org.example.autogestor.model.OrdemServico;
import org.example.autogestor.model.Servico;
import org.example.autogestor.model.Veiculo;
import org.example.autogestor.repository.OrdemServicoRepository;
import org.example.autogestor.repository.ServicoRepository;
import org.example.autogestor.repository.VeiculoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/ordens")
public class OrdemServicoController {

    private final OrdemServicoRepository ordemRepository;
    private final VeiculoRepository veiculoRepository;
    private final ServicoRepository servicoRepository;

    public OrdemServicoController(
            OrdemServicoRepository ordemRepository,
            VeiculoRepository veiculoRepository,
            ServicoRepository servicoRepository) {

        this.ordemRepository = ordemRepository;
        this.veiculoRepository = veiculoRepository;
        this.servicoRepository = servicoRepository;
    }

    @GetMapping
    public String listar(Model model) {

        model.addAttribute(
                "ordens",
                ordemRepository.findAll()
        );

        return "ordens";
    }

    @GetMapping("/novo")
    public String novo(Model model) {

        OrdemServico ordem = new OrdemServico();

        ordem.setDataAbertura(LocalDate.now());
        ordem.setStatus("Aguardando");

        model.addAttribute("ordem", ordem);
        model.addAttribute("veiculos", veiculoRepository.findAll());
        model.addAttribute("servicos", servicoRepository.findAll());

        return "ordem-form";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute OrdemServico ordem,
            @RequestParam Integer veiculoId,
            @RequestParam Integer servicoId) {

        Veiculo veiculo = veiculoRepository
                .findById(veiculoId)
                .orElseThrow();

        Servico servico = servicoRepository
                .findById(servicoId)
                .orElseThrow();

        ordem.setVeiculo(veiculo);
        ordem.setServico(servico);

        if (ordem.getValorTotal() == null) {
            ordem.setValorTotal(servico.getValor());
        }

        ordemRepository.save(ordem);

        return "redirect:/ordens";
    }

    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Integer id,
            Model model) {

        OrdemServico ordem = ordemRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("ordem", ordem);
        model.addAttribute("veiculos", veiculoRepository.findAll());
        model.addAttribute("servicos", servicoRepository.findAll());

        return "ordem-form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {

        ordemRepository.deleteById(id);

        return "redirect:/ordens";
    }
}