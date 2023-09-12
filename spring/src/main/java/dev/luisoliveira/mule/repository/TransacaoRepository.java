package dev.luisoliveira.mule.repository;

import dev.luisoliveira.mule.model.TransacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoModel, UUID> {
    List<TransacaoModel> findByContaIdOrderByDataTransacaoDesc(UUID contaId);

}

