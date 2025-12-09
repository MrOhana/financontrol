package com.financontrol.service;

import com.financontrol.domain.Token;
import com.financontrol.domain.TokenType;
import com.financontrol.domain.User;
import com.financontrol.repository.TokenRepository;
import com.financontrol.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        User user = User.builder().name("Test").email("test@mail.com").password("pass").build();
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User registered = userService.registerUser(user);

        assertNotNull(registered);
        assertEquals("encoded", registered.getPassword());
        assertEquals(User.UserStatus.INACTIVE, registered.getStatus()); // Expect INACTIVE
        verify(userRepository).save(user);
        verify(tokenRepository).save(any(Token.class)); // Verify token creation
        verify(emailService).sendVerificationEmail(eq(registered), anyString()); // Verify email
    }

    @Test
    void shouldThrowExceptionIfEmailExists() {
        User user = User.builder().name("Test").email("test@mail.com").password("pass").build();
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldInitiatePasswordRecovery() {
        User user = User.builder().name("Test").email("test@mail.com").build();
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        userService.initiatePasswordRecovery("test@mail.com");

        verify(tokenRepository).deleteByUserAndType(eq(user), eq(TokenType.RESET_PASSWORD));
        verify(tokenRepository).save(any(Token.class));
        verify(emailService).sendPasswordRecoveryEmail(eq(user), anyString());
    }
}
