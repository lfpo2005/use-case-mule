package dev.luisoliveira.mule.service;

import dev.luisoliveira.mule.model.AccountModel;
import dev.luisoliveira.mule.model.TransactionModel;
import dev.luisoliveira.mule.repository.AccountRepository;
import dev.luisoliveira.mule.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public List<AccountModel> getAllAccounts() {
        return accountRepository.findAll();
    }

    public AccountModel createAccount(AccountModel accountModel) {
        accountModel.setBalance(BigDecimal.ZERO);
        return accountRepository.save(accountModel);
    }

    public AccountModel deposit(UUID accountId, BigDecimal amount) {
        AccountModel accountModel = accountRepository.findById(accountId).orElseThrow();
        accountModel.setBalance(accountModel.getBalance().add(amount));
        createTransaction(accountId, "Deposit", amount);
        return accountRepository.save(accountModel);
    }

    public AccountModel withdraw(UUID accountId, BigDecimal amount) {
        AccountModel accountModel = accountRepository.findById(accountId).orElseThrow();
        accountModel.setBalance(accountModel.getBalance().subtract(amount));
        createTransaction(accountId, "Withdrawal", amount);
        return accountRepository.save(accountModel);
    }

    public AccountModel transfer(UUID fromId, UUID toId, BigDecimal amount) {
        AccountModel fromAccount = withdraw(fromId, amount);
        AccountModel toAccount = deposit(toId, amount);
        createTransaction(fromId, "Transfer", amount.negate());
        createTransaction(toId, "Transfer", amount);
        return fromAccount;
    }

    public TransactionModel createTransaction(UUID accountId, String type, BigDecimal amount) {
        TransactionModel transaction = new TransactionModel();
        transaction.setAccountId(accountId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<TransactionModel> getHistory(UUID accountId) {
        return transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId);
    }

    public AccountModel getBalance(UUID accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }
}

