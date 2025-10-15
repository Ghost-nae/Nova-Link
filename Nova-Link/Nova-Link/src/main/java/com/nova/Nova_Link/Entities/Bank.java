package com.nova.Nova_Link.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.nova.Nova_Link.Entities.Account;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String bankName;
    private String bankAccount;

    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL,  fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();

    public Bank() {
        this.accounts = new ArrayList<>();
    }


    public Bank(String bankName, String bankAccount, ArrayList<Account> accounts) {
        this.bankName = bankName;
        this.bankAccount = bankAccount;
        this.accounts = new  ArrayList<>(accounts);
    }


}