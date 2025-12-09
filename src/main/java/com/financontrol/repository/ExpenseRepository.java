package com.financontrol.repository;

import com.financontrol.domain.Expense;
import com.financontrol.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate,
            LocalDate endDate);
}
