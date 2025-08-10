package com.nova.Nova_Link;

import java.util.List;

public class Bank {
    private String bankName;
    private List<Account> accounts;

    public Bank() {}

    public Bank (String bankName, List<Account> accounts) {
        this.bankName = bankName;
        this.accounts = accounts;
    }

    //Account management
    public void addAccount(Account account) {
        //loop through the accounts list
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == account.getAccountNumber()) {
                System.out.println("This account already exists");
                return;
            }
        }
        accounts.add(account);
    }

    public boolean removeAccount(int accountNumber) {
        //loop through the accounts lists
        for (Account acc : accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                accounts.remove(acc);
                return true;
            }
        }
        return false;
    }

    public Account findAccount(int accountNumber) {
        //loop through the accounts list to find account number
        for (Account acc: accounts) {
            if (acc.getAccountNumber() == accountNumber) {
                return acc;
            }
        }
        return null;
    }

    //Bank Operations

    public boolean deposit(int accountNumber, double amount) {
        for (Account account : accounts) {
            
        }
    }

    public void displayAllAccounts() {

    }


}