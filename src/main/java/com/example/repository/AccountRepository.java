package com.example.repository;

import com.example.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    // Finds an account by username
    // Account findByUsername(String username);

    // Checks if an account exists by username
    // boolean existsByUsername(String username);
}
