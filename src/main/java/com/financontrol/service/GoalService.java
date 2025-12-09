package com.financontrol.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.financontrol.domain.Goal;
import com.financontrol.repository.GoalRepository;

@Service
public class GoalService {

    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public List<Goal> findAll() {
        return goalRepository.findAllByOrderByCreatedAtDesc();
    }

    public Optional<Goal> findById(Long id) {
        return goalRepository.findById(id);
    }

    @Transactional
    public Goal save(Goal goal) {
        return goalRepository.save(goal);
    }

    @Transactional
    public void delete(Long id) {
        goalRepository.deleteById(id);
    }
}
