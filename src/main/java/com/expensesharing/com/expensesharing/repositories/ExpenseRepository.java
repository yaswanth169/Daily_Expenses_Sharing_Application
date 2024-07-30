package com.expensesharing.com.expensesharing.repositories;

import com.expensesharing.com.expensesharing.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByParticipantsUserId(Long userId);
}
