package com.nova.Nova_Link.Service;

import com.nova.Nova_Link.ENUMS.TransactionStatus;
import com.nova.Nova_Link.ENUMS.TransactionType;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Transaction;
import com.nova.Nova_Link.Repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionAuditService {
   private final TransactionRepository transactionRepository;

  @Transactional(Transactional.TxType.REQUIRES_NEW)
  public void recordFailedTransaction(Account senderAccount,
                                        Account recipientAccount,
                                        java.math.BigDecimal amount,
                                        TransactionType type,
                                        TransactionStatus status,
                                        String description){

    Transaction transaction = new Transaction();
    transaction.setSenderAccount(senderAccount);
        transaction.setRecipientAccount(recipientAccount);
        transaction.setAmount(amount);
        transaction.setStatus(status);
        transaction.setDescription(description);
        transaction.setType(type);

        transactionRepository.save(transaction);
  }
}
