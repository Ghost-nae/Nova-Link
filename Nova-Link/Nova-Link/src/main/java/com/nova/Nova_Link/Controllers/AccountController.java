package com.nova.Nova_Link.Controllers;

import com.nova.Nova_Link.Entities.Account;
import com.nova.Nova_Link.Repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;

    @GetMapping("/my")
    public List<Account> getMyAccounts(Authentication authentication) {

        String email = authentication.getName();

        return accountRepository.findByUserEmail(email);
    }
}
