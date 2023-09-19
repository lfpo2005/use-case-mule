
package dev.luisoliveira.mule.controller;

import dev.luisoliveira.mule.model.AccountModel;
import dev.luisoliveira.mule.model.TransactionModel;
import dev.luisoliveira.mule.service.AccountService;
import dev.luisoliveira.mule.service.JmsProducer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JmsProducer jmsProducer;

    @GetMapping
    public ResponseEntity<List<AccountModel>> getAllAccounts() {
        List<AccountModel> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{accountId}/history")
    public ResponseEntity<List<TransactionModel>> getHistory(@PathVariable UUID accountId) {
        List<TransactionModel> history = accountService.getHistory(accountId);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<?> getBalance(@PathVariable UUID accountId) {
        AccountModel account = accountService.getBalance(accountId);
        if (account != null) {
            String message = String.format("Account: %s, Balance: %s",
                    account.getName(), account.getBalance().toString());
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public AccountModel createAccount(@RequestBody AccountModel account) {
        AccountModel createdAccount = accountService.createAccount(account);

        String message = "New account created with ID: " + createdAccount.getAccountId();
        jmsProducer.sendMessage("stg-mule", message);

        System.out.println("Account created: " + createdAccount.toString());
        log.debug("Account created: {}", createdAccount.toString());

        return createdAccount;
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<?> deposit(@PathVariable UUID accountId, @RequestBody Map<String, Object> payload) {
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new ResponseEntity<>("Deposit amount must be positive", HttpStatus.BAD_REQUEST);
        }
        AccountModel updatedAccount = accountService.deposit(accountId, amount);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    @PostMapping("/{accountId}/withdraw")
    public AccountModel withdraw(@PathVariable UUID accountId, @RequestBody Map<String, Object> payload) {
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        return accountService.withdraw(accountId, amount);
    }

    @PostMapping("/transfer/from/{fromId}/to/{toId}")
    public AccountModel transfer(@PathVariable UUID fromId, @PathVariable UUID toId, @RequestBody Map<String, Object> payload) {
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        return accountService.transfer(fromId, toId, amount);
    }
}
