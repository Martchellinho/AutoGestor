package org.example.autogestor.controller;

import org.example.autogestor.model.Servico;
import org.example.autogestor.repository.ServicoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoRepository servicoRepository;

    public ServicoController(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("servicos", servicoRepository.findAll());
        return "servicos";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("servico", new Servico());
        return "servico-form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Servico servico) {
        servicoRepository.save(servico);
        return "redirect:/servicos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {

        Servico servico = servicoRepository
                .findById(id)
                .orElseThrow();

        model.addAttribute("servico", servico);

        return "servico-form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id, Model model) {

        try {
            servicoRepository.deleteById(id);
            servicoRepository.flush();

            return "redirect:/servicos";

        } catch (Exception e) {

            model.addAttribute(
                    "servicos",
                    servicoRepository.findAll()
            );

            model.addAttribute(
                    "erro",
                    "Não é possível excluir este serviço porque existem registros vinculados a ele."
            );

            return "servicos";
        }
    }
}