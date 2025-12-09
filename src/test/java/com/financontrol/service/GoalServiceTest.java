package com.financontrol.service;

import com.financontrol.domain.Goal;
import com.financontrol.repository.GoalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    @Test
    void shouldFindAllGoals() {
        when(goalRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(new Goal()));
        
        List<Goal> goals = goalService.findAll();
        
        assertEquals(1, goals.size());
        verify(goalRepository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void shouldSaveGoal() {
        Goal goal = new Goal();
        goalService.save(goal);
        verify(goalRepository).save(goal);
    }
}
