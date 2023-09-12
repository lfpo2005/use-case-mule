package dev.luisoliveira.mule.service;

import dev.luisoliveira.mule.model.ContaModel;
import dev.luisoliveira.mule.model.TransacaoModel;
import dev.luisoliveira.mule.repository.ContaRepository;
import dev.luisoliveira.mule.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<ContaModel> buscarTodasContas() {
        return contaRepository.findAll();
    }

    public ContaModel criarConta (ContaModel contaModel) {
        contaModel.setSaldo(BigDecimal.ZERO);
        return contaRepository.save(contaModel);
    }

    public ContaModel depositar(UUID contaId, BigDecimal valor) {
        ContaModel contaModel = contaRepository.findById(contaId).orElseThrow();
        contaModel.setSaldo(contaModel.getSaldo().add(valor));
        criarTransacao(contaId, "Deposito", valor);
        return contaRepository.save(contaModel);
    }

    public ContaModel sacar(UUID contaId, BigDecimal valor) {
        ContaModel contaModel = contaRepository.findById(contaId).orElseThrow();
        contaModel.setSaldo(contaModel.getSaldo().subtract(valor));
        criarTransacao(contaId, "Saque", valor);
        return contaRepository.save(contaModel);
    }

    public ContaModel transferir(UUID deId, UUID paraId, BigDecimal valor) {
        ContaModel contaDe = sacar(deId, valor);
        ContaModel contaPara = depositar(paraId, valor);
        criarTransacao(deId, "Transferencia", valor.negate());
        criarTransacao(paraId, "Transferencia", valor);
        return contaDe;
    }

    public TransacaoModel criarTransacao(UUID contaId, String tipo, BigDecimal valor) {
        TransacaoModel transacao = new TransacaoModel();
        transacao.setContaId(contaId);
        transacao.setTipo(tipo);
        transacao.setValor(valor);
        transacao.setDataTransacao(LocalDateTime.now());
        return transacaoRepository.save(transacao);
    }

    public List<TransacaoModel> buscarHistorico(UUID contaId) {
        return transacaoRepository.findByContaIdOrderByDataTransacaoDesc(contaId);
    }

    public ContaModel consultarSaldo(UUID contaId) {
        return contaRepository.findById(contaId).orElse(null);
    }

}

