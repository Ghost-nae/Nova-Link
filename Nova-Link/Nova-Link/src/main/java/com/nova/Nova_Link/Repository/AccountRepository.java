package com.nova.Nova_Link.Repository;

import com.nova.Nova_Link.ENUMS.AccountStatus;
import com.nova.Nova_Link.ENUMS.AccountType;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByBank(Bank bank);
    List<Account> findByStatus(AccountStatus status);
    List<Account> findByType(AccountType type);
}
