package com.projecthub.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    String email;

    String hashedPassword;

    Instant createdAt;

    protected Account() {}

    /**
     * Creates an account with the specified email address and hashed password.
     * The creation timestamp is initialized to the current instant.
     *
     * @param email the unique email address associated with the account
     * @param hashedPassword the hashed password used to authenticate the account
     */
    public Account(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.createdAt = Instant.now();
    }
}
