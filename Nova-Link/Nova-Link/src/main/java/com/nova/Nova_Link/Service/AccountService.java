package com.nova.Nova_Link.Service;

import com.nova.Nova_Link.ENUMS.AccountStatus;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Repository.AccountRepository;
import com.nova.Nova_Link.Repository.BankRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;
    private final BankService bankService;

    //Account Management
   public void createAccount(Long bankId, Account account){
       if (bankRepository.findById(bankId).isPresent()){
           account.setBank(bankRepository.findById(bankId).get());
       }
       account.setStatus(AccountStatus.Active);
       accountRepository.save(account);
   }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }


    public List<Account> getAccounts(){
       return accountRepository.findAll();
   }

   public void deleteAccount(Long accountId){
      if (accountRepository.findById(accountId).isPresent()) {
            accountRepository.deleteById(accountId);
      } else {
          throw new RuntimeException("Account not found");
      }

   }

   //Transaction Operations
   public void deposit(String accountNumber, BigDecimal amount) {
       Account account = accountRepository.findByAccountNumber(accountNumber)
               .orElseThrow(() -> new RuntimeException("Account not found"));

       if (account.getStatus() != AccountStatus.Active) {
           throw new RuntimeException("Account not Active");
       }

       account.setBalance(account.getBalance().add(amount));
       accountRepository.save(account);
   }


}
