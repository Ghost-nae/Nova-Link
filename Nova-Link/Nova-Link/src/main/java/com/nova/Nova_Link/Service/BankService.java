package com.nova.Nova_Link.Service;

import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Entities.Bank;
import com.nova.Nova_Link.Repository.AccountRepository;
import com.nova.Nova_Link.Repository.BankRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BankService {
    private final BankRepository bankRepository;
    private final AccountRepository accountRepository;

    //Bank Management
    public void createBank(Bank bank){
        if (bankRepository.existsByBankName(bank.getBankName())) {
            throw new RuntimeException("Bank with name " + bank.getBankName() + " already exists");
        } else {
            bankRepository.save(bank);
        }
    }

    public Bank getBankById(Long id){
        if (bankRepository.existsById(id)) {
            return bankRepository.findById(id).get();
        }
        return bankRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bank not found"));
    }

    public List<Bank> getAllBanks(){
        return bankRepository.findAll();
    }

    public void deleteBankById(Long id){
        Optional<Bank> bank = bankRepository.findById(id);
        if (bank.isPresent()) {
            bankRepository.deleteById(id);
        } else {
            throw new RuntimeException("Bank with id " + id + " does not exist");
        }
    }

    //Account-Bank relationship operations

    public void addAccountToBank(Long bankId, Account account){
        Optional<Bank> bank = bankRepository.findById(bankId);
        if (bank.isPresent()) {
            account.setBank(bank.get());
        }else {
            throw new RuntimeException("Bank with id " + bankId + " does not exist");
        }
        accountRepository.save(account);
    }

    public List<Account> getAccountsByBankId(Long bankId){
        Optional<Bank> bank = bankRepository.findById(bankId);
        if (bank.isPresent()) {
            return accountRepository.findByBank(bank.get());
        }
        return null;
    }
}
