package com.financontrol.repository;

import com.financontrol.domain.Income;
import com.financontrol.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findAllByUserAndDateBetweenOrderByDateDesc(User user, LocalDate startDate,
            LocalDate endDate);
}
