package com.nova.Nova_Link.Repository;

import com.nova.Nova_Link.ENUMS.TransactionType;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByTransactionId(UUID transactionId);
    List<Transaction> findBySenderAccount(Account senderAccount);
    List<Transaction> findByRecipientAccount(Account recipientAccount);
    List<Transaction> findBySenderAccountOrRecipientAccount(Account senderAccount, Account recipientAccount);
}

