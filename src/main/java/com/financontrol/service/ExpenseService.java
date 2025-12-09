package com.financontrol.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import com.financontrol.domain.Expense;
import com.financontrol.domain.User;
import com.financontrol.repository.ExpenseRepository;
import com.financontrol.repository.UserRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public ExpenseService(ExpenseRepository expenseRepository,
            UserRepository userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }

    public List<Expense> findAll(LocalDate startDate, LocalDate endDate) {
        return expenseRepository.findAllByUserAndDateBetweenOrderByDateDesc(getCurrentUser(), startDate, endDate);
    }

    public List<Expense> findAllInMonth(LocalDate date) {
        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endDate = date.withDayOfMonth(date.lengthOfMonth());
        return findAll(startDate, endDate);
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Transactional
    public Expense save(Expense expense) {
        if (expense.getUser() == null) {
            expense.setUser(getCurrentUser());
        }
        return expenseRepository.save(expense);
    }

    @Transactional
    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }

    User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
