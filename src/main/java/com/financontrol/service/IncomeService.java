package com.financontrol.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;

import com.financontrol.domain.Income;
import com.financontrol.domain.User;
import com.financontrol.repository.IncomeRepository;
import com.financontrol.repository.UserRepository;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    public IncomeService(IncomeRepository incomeRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
    }

    public List<Income> findAll(LocalDate startDate, LocalDate endDate) {
        return incomeRepository.findAllByUserAndDateBetweenOrderByDateDesc(getCurrentUser(), startDate, endDate);
    }

    public Optional<Income> findById(Long id) {
        return incomeRepository.findById(id);
    }

    @Transactional
    public Income save(Income income) {
        if (income.getUser() == null) {
            income.setUser(getCurrentUser());
        }
        return incomeRepository.save(income);
    }

    @Transactional
    public void delete(Long id) {
        incomeRepository.deleteById(id);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication()
                .getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }
}
