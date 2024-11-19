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
        this.accountRepository = accountRepository;
    }

    // Method to create a new message
    public Message createMessage(Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().trim().isEmpty()) {
            throw new IllegalArgumentException("Message text cannot be empty.");
        }
        if (message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Message text cannot exceed 255 characters.");
        }

        // Validate user existence
        Optional<Account> accountOptional = accountRepository.findById(message.getPostedBy());
        if (!accountOptional.isPresent()) {
            throw new IllegalArgumentException("User not found.");
        }

        // Save and return the message
        return messageRepository.save(message);
    }

    // Method to retrieve all messages
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Method to retrieve a message by Id
    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
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
    public int deleteMessageById(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1; // Successfully deleted one row
        } else {
            return 0; // No rows were deleted as the message ID doesn't exist
        }
    }

}
