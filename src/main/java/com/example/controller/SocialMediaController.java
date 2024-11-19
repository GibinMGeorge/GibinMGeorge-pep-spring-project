package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling social media-related endpoints.
 */
@RestController
// @RequestMapping("/api") // Base URL for the endpoints
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Endpoint to create a new account
    @PostMapping("/register")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        try {
            // Check if the username already exists
            if (accountService.existsByUsername(account.getUsername())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT); // Return 409 if username exists
            }

            // If the username does not exist, create the account
            Account createdAccount = accountService.createAccount(account);
            return new ResponseEntity<>(createdAccount, HttpStatus.OK);
        } catch (Exception e) {
            // Log the error and return a 500 status for unexpected errors
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint for login
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account loginRequest) {
        try {
            // Check if the account exists by username
            Account account = accountService.getAccountByUsername(loginRequest.getUsername());
            if (account != null && account.getPassword().equals(loginRequest.getPassword())) {
                return new ResponseEntity<>(account, HttpStatus.OK); // Return 200 if login is successful
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Return 401 if username or password is invalid
            }
        } catch (Exception e) {
            // Log the error and return a 500 status for unexpected errors
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Endpoint to retrieve all accounts
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Endpoint to retrieve a single account by ID
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Integer id) {
        Account account = accountService.getAccountById(id);
        return account != null ? new ResponseEntity<>(account, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint to create a new message
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            // Create message and return the result
            Message createdMessage = messageService.createMessage(message);
            return new ResponseEntity<>(createdMessage, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Return 400 status for validation errors
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Return 500 status for unexpected errors
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint to retrieve all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // Endpoint to retrieve messages by user ID
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByAccountId(@PathVariable Integer accountId) {
        List<Message> messages = messageService.getMessagesByAccountId(accountId);
        return messages != null ? new ResponseEntity<>(messages, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint to retrieve a message by ID
    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer id) {
        Message message = messageService.getMessageById(id);
        return message != null 
            ? new ResponseEntity<>(message, HttpStatus.OK) 
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // Endpoint to delete a message by ID
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable Integer id) {
        int rowsDeleted = messageService.deleteMessageById(id);
    
        // Return appropriate response
        if (rowsDeleted > 0) {
            return ResponseEntity.ok(rowsDeleted); // Message was found and deleted, return count
        } else {
            return ResponseEntity.ok(""); // Message not found, return empty body
        }
    }

    // Endpoint to update account information
    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer id, @RequestBody Account updatedAccount) {
        Account account = accountService.updateAccount(id, updatedAccount);
        return account != null ? new ResponseEntity<>(account, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint to update a message by ID
    @PatchMapping("/messages/{id}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer id, @RequestBody Message updatedMessage) {
        try {
            Message updated = messageService.updateMessage(id, updatedMessage);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}