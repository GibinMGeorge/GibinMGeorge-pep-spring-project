package com.example.service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing messages.
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository; 

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository; // Initialize the accountRepository
    }

    // Method to create a new message
    public Message createMessage(Message message) {
        // Ensure the message is valid (You may do additional validation here)
        if (message.getMessageText() == null || message.getMessageText().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be empty.");
        }
        
        // Find the user by ID to check if they exist
        Optional<Account> accountOptional = accountRepository.findById(message.getPostedBy());
        if (!accountOptional.isPresent()) {
            throw new IllegalArgumentException("User not found.");
        }

        // If everything is valid, save and return the message
        return messageRepository.save(message);
    }

    // Method to retrieve all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Method to retrieve a message by ID
    public Message getMessageById(Integer id) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        return messageOptional.orElseThrow(() -> new RuntimeException("Message not found with ID: " + id));
    }

    // Method to retrieve all messages posted by a specific account
    public List<Message> getMessagesByAccountId(Integer accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    // Method to update a message by ID
    public Message updateMessage(Integer id, Message updatedMessage) {
        Optional<Message> messageOptional = messageRepository.findById(id);
        if (messageOptional.isPresent()) {
            Message existingMessage = messageOptional.get();
            existingMessage.setMessageText(updatedMessage.getMessageText());
            existingMessage.setTimePostedEpoch(updatedMessage.getTimePostedEpoch());
            return messageRepository.save(existingMessage);
        } else {
            throw new RuntimeException("Message not found with ID: " + id);
        }
    }

    // Method to delete a message by ID
    public boolean deleteMessageById(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        } else {
            throw new RuntimeException("Message not found with ID: " + id);
        }
    }
}
