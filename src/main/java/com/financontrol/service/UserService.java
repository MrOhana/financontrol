package com.financontrol.service;

import com.financontrol.domain.Token;
import com.financontrol.domain.TokenType;
import com.financontrol.domain.User;
import com.financontrol.repository.TokenRepository;
import com.financontrol.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Transactional
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        // 1. Save user as INACTIVE
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(User.UserStatus.INACTIVE);
        user.setRole(User.Role.USER);
        User savedUser = userRepository.save(user);

        // 2. Create Verification Token
        String tokenString = UUID.randomUUID().toString();
        Token token = Token.builder()
                .token(tokenString)
                .type(TokenType.VERIFICATION)
                .user(savedUser)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(token);

        // 3. Send Verification Email
        emailService.sendVerificationEmail(savedUser, tokenString);

        return savedUser;
    }

    @Transactional
    public void verifyUser(String tokenString) {
        Token token = tokenRepository.findByTokenAndType(tokenString, TokenType.VERIFICATION)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired verification token"));

        User user = token.getUser();
        user.setStatus(User.UserStatus.ACTIVE);
        userRepository.save(user);

        // Clean up token
        tokenRepository.delete(token);
    }

    @Transactional
    public void initiatePasswordRecovery(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            // Delete any existing reset tokens for this user
            tokenRepository.deleteByUserAndType(user, TokenType.RESET_PASSWORD);

            // Create new token
            String tokenString = UUID.randomUUID().toString();
            Token token = Token.builder()
                    .token(tokenString)
                    .type(TokenType.RESET_PASSWORD)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(1)) // 1 hour for password reset
                    .build();
            tokenRepository.save(token);

            // Send email
            emailService.sendPasswordRecoveryEmail(user, tokenString);
        });
    }

    @Transactional
    public void resetPassword(String tokenString, String newPassword) {
        Token token = tokenRepository.findByTokenAndType(tokenString, TokenType.RESET_PASSWORD)
                .filter(t -> t.getExpiryDate().isAfter(LocalDateTime.now()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset token"));

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Clean up token
        tokenRepository.delete(token);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
