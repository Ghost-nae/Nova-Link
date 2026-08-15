package com.nova.Nova_Link.Service;

import com.nova.Nova_Link.ENUMS.AccountStatus;
import com.nova.Nova_Link.Entities.User;
import com.nova.Nova_Link.Entities.Bank;
import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Repository.AccountRepository;
import com.nova.Nova_Link.Repository.BankRepository;
import com.nova.Nova_Link.Repository.UserRepository;
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
    private final UserRepository userRepository;

    //Account Management
   public void createAccount(Long bankId, Account account, String userEmail){
       User user = UserRepository.findByEmail(userEmail)
           .orElseThrow(() -> new RuntimeException("User not found"));
       Bank bank = bankRepository.findById(bankId)
            .orElseThrow(() -> new RuntimeException("Bank not found"));

        account.setUser(user);
        account.setBank(bank);
        account.setStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setAccountNumber(generateAccountNumber());
    
        accountRepository.save(account);
   }

    public String generateAccountNumber() {
        String accountNumber;

        do {
            accountNumber = String.valueOf(
                100000000L + (long) (Math.random() * 900000000L)
            );
        } while (accountRepository.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
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

       if (account.getStatus() != AccountStatus.ACTIVE) {
           throw new RuntimeException("Account not Active");
       }

       account.setBalance(account.getBalance().add(amount));
       accountRepository.save(account);
   }

   public void pay(String accountNumber, BigDecimal amount) {
       Account account = accountRepository.findByAccountNumber(accountNumber)
               .orElseThrow(() -> new RuntimeException("Account not found"));
       if (account.getStatus() != AccountStatus.ACTIVE) {
           throw new RuntimeException("Account not Active");
       }
       if (account.getBalance().compareTo(amount) < 0) {
          throw new RuntimeException("Insufficient funds");
       } else  {
           account.setBalance(account.getBalance().subtract(amount));
       }
       accountRepository.save(account);
   }
}
