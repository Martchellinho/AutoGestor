package org.example.autogestor.repository;

import org.example.autogestor.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository
        extends JpaRepository<Veiculo, Integer> {
}