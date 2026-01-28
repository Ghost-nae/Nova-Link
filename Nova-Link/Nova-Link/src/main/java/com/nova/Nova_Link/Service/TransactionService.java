package com.nova.Nova_Link.Service;

import com.nova.Nova_Link.ENUMS.AccountStatus;
import com.nova.Nova_Link.ENUMS.TransactionStatus;
import com.nova.Nova_Link.ENUMS.TransactionType;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Transaction;
import com.nova.Nova_Link.Repository.AccountRepository;
import com.nova.Nova_Link.Repository.BankRepository;
import com.nova.Nova_Link.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final BankRepository bankRepository;
    private final AccountService accountService;



    @Transactional
    public String transferFunds(String senderAccountNumber,
                                String recipientAccountNumber,
                                BigDecimal amount) {

        // 1. Load accounts
        Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber)
                .orElseThrow(() -> new IllegalStateException("Sender account not found"));

        Account recipientAccount = accountRepository.findByAccountNumber(recipientAccountNumber)
                .orElseThrow(() -> new IllegalStateException("Recipient account not found"));

        // 2. Validate sender status
        if (senderAccount.getStatus() != AccountStatus.Active) {
            throw new IllegalStateException("Sender account is not active");
        }

        // 3. Validate amount
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount");
        }

        // 4. Validate balance
        if (senderAccount.getBalance().compareTo(amount) < 0) {
            // Record failed transaction
            recordTransaction(
                    senderAccount,
                    recipientAccount,
                    amount,
                    TransactionType.TRANSFER,
                    TransactionStatus.FAILED,
                    "Insufficient funds"
            );
            throw new IllegalStateException("Insufficient funds");
        }

        // 5. Perform balance updates
        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

        // 6. Record successful transaction
        recordTransaction(
                senderAccount,
                recipientAccount,
                amount,
                TransactionType.TRANSFER,
                TransactionStatus.SUCCESS,
                "Transfer completed successfully"
        );

        return "Transfer successful";
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

    @Transactional
    public String reversalOfFunds(UUID originalTransactionId) {

        Transaction originalTransaction = transactionRepository.findByTransactionId(originalTransactionId)
                .orElseThrow(() -> new RuntimeException("Original transaction not found"));

        if (originalTransaction.getStatus() != TransactionStatus.SUCCESS) {
            throw new RuntimeException("Transaction cannot be reversed");
        }

        Account originalSender = originalTransaction.getSenderAccount();
        Account originalRecipient = originalTransaction.getRecipientAccount();
        BigDecimal amount = originalTransaction.getAmount();

        if (originalRecipient.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Recipient has insufficient funds for reversal");
        }

        // Reverse balance movement
        originalRecipient.setBalance(originalRecipient.getBalance().subtract(amount));
        originalSender.setBalance(originalSender.getBalance().add(amount));

        accountRepository.save(originalRecipient);
        accountRepository.save(originalSender);

        // Mark original transaction as reversed
        originalTransaction.setStatus(TransactionStatus.REVERSED);
        transactionRepository.save(originalTransaction);

        // Record reversal transaction
        recordTransaction(
                originalRecipient,
                originalSender,
                amount,
                TransactionType.REVERSAL,
                TransactionStatus.SUCCESS,
                "Reversal of transaction " + originalTransactionId
        );

        return "Reversal successful";
    }


}
