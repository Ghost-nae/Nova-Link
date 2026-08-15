package com.nova.Nova_Link.Controllers;

import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nova.Nova_Link.Service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nova.Nova_Link.DTO.CreateAccountRequest;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @GetMapping("/my")
    public List<Account> getMyAccounts(Authentication authentication) {

        String email = authentication.getName();

        return accountRepository.findByUserEmail(email);
    }

    @PostMapping
    public ResponseEntity<String> createAccount(@RequestParam Long bankId, 
                                               @RequestBody CreateAccountRequest request,
                                               Authentication authentication){
        String email = authentication.getName();
        accountService.createAccount(bankId, request, email);
        return ResponseEntity.ok("Account created successfully")
                                               }
}
