package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing accounts.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Method to create a new account
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Method to retrieve all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Method to retrieve an account by ID
    public Account getAccountById(Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        return accountOptional.orElse(null);
    }

    // Method to update an account by ID
    public Account updateAccount(Integer id, Account updatedAccount) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account existingAccount = accountOptional.get();
            existingAccount.setUsername(updatedAccount.getUsername());
            existingAccount.setPassword(updatedAccount.getPassword());
            return accountRepository.save(existingAccount);
        } else {
            return null;
        }
    }

    // Method to delete an account by ID
    public boolean deleteAccountById(Integer id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
