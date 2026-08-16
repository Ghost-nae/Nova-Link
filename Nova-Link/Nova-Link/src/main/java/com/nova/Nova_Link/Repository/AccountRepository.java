package com.nova.Nova_Link.Repository;

import com.nova.Nova_Link.ENUMS.AccountStatus;
import com.nova.Nova_Link.ENUMS.AccountType;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByBank(Bank bank);
    List<Account> findByStatus(AccountStatus status);
    List<Account> findByType(AccountType type);
    List<Account> findByUserEmail(String email);
    Optional<Account> findByAccountNumberAndUserEmail(
        String accountNumber,
        String email
    );
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           SELECT a 
           FROM Account a
           WHERE a.accountNumber = :accountNumber 
    """)
    Optional<Account> findByAccountNumberForUpdate(@Param("accountNumber") String accountNumber);
}
