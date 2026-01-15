package com.nova.Nova_Link.Service;

import com.nova.Nova_Link.ENUMS.AccountStatus;
import com.nova.Nova_Link.ENUMS.TransactionStatus;
import com.nova.Nova_Link.ENUMS.TransactionType;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Transaction;
import com.nova.Nova_Link.Repository.AccountRepository;
import com.nova.Nova_Link.Repository.BankRepository;
import com.nova.Nova_Link.Repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {
    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;
    private BankRepository bankRepository;
    private AccountService accountService;
    private Account account;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, BankRepository bankRepository, AccountService accountService, Account account) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.bankRepository = bankRepository;
        this.accountService = accountService;
        this.account = account;
    }

    public String transferFunds(Account senderAccount, Account recipientAccount, BigDecimal amount) {
        Optional<Account> accountSender = accountRepository.findByAccountNumber(senderAccount.getAccountNumber());
        Optional<Account> accountReceiver = accountRepository.findByAccountNumber(recipientAccount.getAccountNumber());

        if (!accountSender.isPresent() ||
                !accountReceiver.isPresent()) {
            throw new IllegalStateException("Sender Account number or Recipient Account number not found");
        }
        if (account.getStatus() != AccountStatus.Active) {
            throw new RuntimeException("Account not Active");
        }
        if (accountSender.amount > accountSender.balance) {
            throw new RuntimeException("Insufficient funds");
        }
        if (accountSender.amount <= accountSender.balance) {
            accountSender.balance -= accountSender.amount;
            accountReceiver.balance += accountSender.amount;
            System.out.println(accountSender.amount + "has been successfully sent")
        }
        transactionRepository.save(transaction);
    }

    protected void recordTransaction(Account senderAccount, Account recipientAccount, BigDecimal amount, TransactionType type, TransactionStatus status, String description) {
        Transaction transaction = new Transaction();
        UUID transactionId = UUID.randomUUID();
        transaction.setSenderAccount(senderAccount);
        transaction.setRecipientAccount(recipientAccount);
        transaction.setAmount(amount);
        transaction.setStatus(status);
        transaction.setDescription(description);
        transaction.setType(type);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

}
