package com.financontrol.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.financontrol.domain.User;
import com.financontrol.repository.ExpenseRepository;
import com.financontrol.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    // Manually instantiated in test
    private ExpenseService expenseService;

    @Test
    void shouldFindAllInMonth_AllMonths() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@test.com");

        // Manually instantiate and override getCurrentUser
        expenseService = new ExpenseService(expenseRepository, userRepository) {
            @Override
            User getCurrentUser() {
                return mockUser;
            }
        };

        int year = 2025;

        // Iterate through all months
        for (Month month : Month.values()) {
            // Pick a random day in the month (e.g. 15th)
            LocalDate dateInMonth = LocalDate.of(year, month, 15);

            // Act
            expenseService.findAllInMonth(dateInMonth);

            // Calculate expected start (1st) and end (last day)
            LocalDate expectedStart = LocalDate.of(year, month, 1);
            LocalDate expectedEnd = expectedStart.withDayOfMonth(expectedStart.lengthOfMonth());

            // Assert
            verify(expenseRepository).findAllByUserAndDateBetweenOrderByDateDesc(mockUser, expectedStart, expectedEnd);
        }

        // Verify we called the repo 12 times (once per month)
        verify(expenseRepository, times(12)).findAllByUserAndDateBetweenOrderByDateDesc(any(), any(), any());
    }
}
