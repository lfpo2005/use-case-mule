package dev.luisoliveira.mule.controller;

import dev.luisoliveira.mule.model.ContaModel;
import dev.luisoliveira.mule.model.TransacaoModel;
import dev.luisoliveira.mule.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/contas")
public class ContaController {


    @Autowired
    private ContaService contaService;

    @GetMapping
    public ResponseEntity<List<ContaModel>> buscarTodasContas() {
        List<ContaModel> contas = contaService.buscarTodasContas();
        return new ResponseEntity<>(contas, HttpStatus.OK);
    }

    @GetMapping("/{contaId}/historico")
    public ResponseEntity<List<TransacaoModel>> buscarHistorico(@PathVariable UUID contaId) {
        List<TransacaoModel> historico = contaService.buscarHistorico(contaId);
        return new ResponseEntity<>(historico, HttpStatus.OK);
    }

    @GetMapping("/{contaId}/saldo")
    public ResponseEntity<?> consultarSaldo(@PathVariable UUID contaId) {
        ContaModel conta = contaService.consultarSaldo(contaId);
        if (conta != null) {
            String mensagem = String.format("Conta: %s, Saldo: %s",
                    conta.getNome(), conta.getSaldo().toString());
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Conta não encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ContaModel criarConta(@RequestBody ContaModel conta) {
        return contaService.criarConta(conta);
    }

    @PostMapping("/{contaId}/depositar")
    public ResponseEntity<?> depositar(@PathVariable UUID contaId, @RequestBody Map<String, Object> payload) {
        BigDecimal valor = new BigDecimal(payload.get("valor").toString());

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            return new ResponseEntity<>("Valor de depósito deve ser positivo", HttpStatus.BAD_REQUEST);
        }
        ContaModel contaAtualizada = contaService.depositar(contaId, valor);
        return new ResponseEntity<>(contaAtualizada, HttpStatus.OK);
    }


    @PostMapping("/{contaId}/sacar")
    public ContaModel sacar(@PathVariable UUID contaId, @RequestBody Map<String, Object> payload) {
        BigDecimal valor = new BigDecimal(payload.get("valor").toString());
        return contaService.sacar(contaId, valor);
    }

    @PostMapping("/transferir/de/{deId}/para/{paraId}")
    public ContaModel transferir(@PathVariable UUID deId, @PathVariable UUID paraId, @RequestBody Map<String, Object> payload) {
        BigDecimal valor = new BigDecimal(payload.get("valor").toString());
        return contaService.transferir(deId, paraId, valor);
    }
}
