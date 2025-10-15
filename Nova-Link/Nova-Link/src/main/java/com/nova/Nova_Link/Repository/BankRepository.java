package com.nova.Nova_Link.Repository;

import com.nova.Nova_Link.Entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank,Long> {
    Optional<Bank> findByBankName(String bankName);
    boolean existsByBankName(String bankName);
}
