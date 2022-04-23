package com.codewithroy.resttester.expense.repo;

import com.codewithroy.resttester.expense.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUserName(String userName);
}
