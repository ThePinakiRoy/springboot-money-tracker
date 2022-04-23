package com.codewithroy.resttester.expense.repo;

import com.codewithroy.resttester.expense.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Person, Long> {
}
