package com.codewithroy.resttester.expense.repo;

import com.codewithroy.resttester.expense.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    Optional<Expense> findByExpenseId(Long id);
}
