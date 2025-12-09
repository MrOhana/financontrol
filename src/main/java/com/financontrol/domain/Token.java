package com.financontrol.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Token() {
    }

    public Token(Long id, String token, TokenType type, User user, LocalDateTime expiryDate, LocalDateTime createdAt) {
        this.id = id;
        this.token = token;
        this.type = type;
        this.user = user;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }

    public static TokenBuilder builder() {
        return new TokenBuilder();
    }

    public static class TokenBuilder {
        private Long id;
        private String token;
        private TokenType type;
        private User user;
        private LocalDateTime expiryDate;
        private LocalDateTime createdAt;

        public TokenBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TokenBuilder token(String token) {
            this.token = token;
            return this;
        }

        public TokenBuilder type(TokenType type) {
            this.type = type;
            return this;
        }

        public TokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public TokenBuilder expiryDate(LocalDateTime expiryDate) {
            this.expiryDate = expiryDate;
            return this;
        }

        public TokenBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Token build() {
            return new Token(id, token, type, user, expiryDate, createdAt);
        }
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
