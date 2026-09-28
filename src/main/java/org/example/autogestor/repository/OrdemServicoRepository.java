package org.example.autogestor.repository;

import org.example.autogestor.model.OrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdemServicoRepository
        extends JpaRepository<OrdemServico, Integer> {
}