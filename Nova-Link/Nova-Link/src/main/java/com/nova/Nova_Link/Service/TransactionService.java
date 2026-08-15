package com.nova.Nova_Link.Service;

import com.nova.Nova_Link.ENUMS.AccountStatus;
import com.nova.Nova_Link.ENUMS.TransactionStatus;
import com.nova.Nova_Link.ENUMS.TransactionType;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Transaction;
import com.nova.Nova_Link.Repository.AccountRepository;
import com.nova.Nova_Link.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.nova.Nova_Link.Service.TransactionAuditService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionAuditService transactionAuditService;

    // Transferring of funds

    @Transactional
   public String transferFunds(
        String senderAccountNumber,
        String recipientAccountNumber,
        BigDecimal amount,
        String userEmail
    ) {

        Account senderAccount = accountRepository
        .findByAccountNumberAndUserEmail(
                senderAccountNumber,
                userEmail
        )
        .orElseThrow(() ->
                new IllegalStateException(
                        "Sender account not found"
                ));

        Account recipientAccount = accountRepository.findByAccountNumber(recipientAccountNumber)
                .orElseThrow(() -> new IllegalStateException("Recipient account not found"));

        if (senderAccountNumber.equals(recipientAccountNumber)) {
            throw new IllegalArgumentException("Sender and recipient accounts cannot be the same");
        }
        if (senderAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Sender account is not active");
        }

        if (recipientAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Recipient account is not active");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount");
        }


        if (senderAccount.getBalance().compareTo(amount) < 0) {

            transactionAuditService.recordFailedTransaction(
                    senderAccount,
                    recipientAccount,
                    amount,
                    TransactionType.TRANSFER,
                    TransactionStatus.FAILED,
                    "Insufficient funds"
            );

            throw new IllegalStateException("Insufficient funds");
        }

        senderAccount.setBalance(senderAccount.getBalance().subtract(amount));
        recipientAccount.setBalance(recipientAccount.getBalance().add(amount));

        accountRepository.save(senderAccount);
        accountRepository.save(recipientAccount);

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

    //Record of transactions

    protected void recordTransaction(Account senderAccount,
                                     Account recipientAccount,
                                     BigDecimal amount,
                                     TransactionType type,
                                     TransactionStatus status,
                                     String description) {

        Transaction transaction = new Transaction();
        transaction.setSenderAccount(senderAccount);
        transaction.setRecipientAccount(recipientAccount);
        transaction.setAmount(amount);
        transaction.setStatus(status);
        transaction.setDescription(description);
        transaction.setType(type);

        transactionRepository.save(transaction);
    }

    //Reversal of funds\

    @Transactional
    public String reversalOfFunds(UUID originalTransactionId) {

        Transaction originalTransaction = transactionRepository.findByTransactionId(originalTransactionId)
                .orElseThrow(() -> new IllegalStateException("Original transaction not found"));

        if (originalTransaction.getStatus() != TransactionStatus.SUCCESS) {
            throw new IllegalStateException("Transaction cannot be reversed");
        }

        Account originalSender = originalTransaction.getSenderAccount();
        Account originalRecipient = originalTransaction.getRecipientAccount();
        BigDecimal amount = originalTransaction.getAmount();

        if (originalRecipient.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Recipient has insufficient funds for reversal");
        }

        originalRecipient.setBalance(originalRecipient.getBalance().subtract(amount));
        originalSender.setBalance(originalSender.getBalance().add(amount));

        accountRepository.save(originalRecipient);
        accountRepository.save(originalSender);

        originalTransaction.setStatus(TransactionStatus.REVERSED);
        transactionRepository.save(originalTransaction);

        recordTransaction(
                originalRecipient,
                originalSender,
                amount,
                originalTransaction
        );

        return "Reversal successful";
    }

    protected void recordReversalTransaction(Account senderAccount,
        Account recipientAccount,
        BigDecimal amount,
        Transaction originalTransaction) {

        Transaction reversal = new Transaction();

        reversal.setSenderAccount(senderAccount);
        reversal.setRecipientAccount(recipientAccount);
        reversal.setAmount(amount);
        reversal.setStatus(TransactionStatus.SUCCESS);
        reversal.setType(TransactionType.REVERSAL);
        reversal.setDescription("Reversal of transaction " +
            originalTransaction.getTransactionId());
        reversal.setOriginalTransaction(originalTransaction);

        transactionRepository.save(reversal);
    }

    //My transactions
    public List<Transaction> getMyTransactions(String email) {

    List<Account> accounts =
            accountRepository.findByUserEmail(email);

    return accounts.stream()
            .flatMap(account ->
                    transactionRepository
                            .findBySenderAccountOrRecipientAccount(
                                    account,
                                    account
                            )
                            .stream()
            )
            .distinct()
            .toList();
}
}
