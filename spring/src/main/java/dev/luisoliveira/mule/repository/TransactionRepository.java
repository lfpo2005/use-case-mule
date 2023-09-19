package dev.luisoliveira.mule.repository;

import dev.luisoliveira.mule.model.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {
    List<TransactionModel> findByAccountIdOrderByTransactionDateDesc(UUID contaId);

}

