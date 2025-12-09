package com.financontrol.repository;

import com.financontrol.domain.Token;
import com.financontrol.domain.TokenType;
import com.financontrol.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByTokenAndType(String token, TokenType type);

    void deleteByUserAndType(User user, TokenType type);
}
