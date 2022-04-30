package com.codewithroy.resttester.expense.service;

import com.codewithroy.resttester.expense.model.Expense;
import com.codewithroy.resttester.expense.repo.ExpenseRepository;
import com.codewithroy.resttester.expense.web.dto.ExpenseDTO;
import com.codewithroy.resttester.expense.web.exception.ExpenseException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Builder
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Transactional
    public ExpenseDTO createPost(ExpenseDTO expenseDTO) {
        Expense expense = dtoToMap(expenseDTO);
        expense = expenseRepository.save(expense);
        expenseDTO.setExpenseId(expense.getExpenseId());
        return expenseDTO;
    }

    @Transactional(readOnly = true)
    public List<ExpenseDTO> getAll() {
       return expenseRepository
                .findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ExpenseDTO updateExpense(ExpenseDTO expenseDTO) {
        expenseRepository.findByExpenseId(expenseDTO.getExpenseId()).orElseThrow(() -> new ExpenseException("Person not Found"));
        Expense expense = expenseRepository.save(dtoToMap(expenseDTO));
        return mapToDto(expense);
    }

    @Transactional(readOnly = true)
    public ExpenseDTO getExpenseById(Long id) {
        Expense expense = expenseRepository.findByExpenseId(id).orElseThrow(() -> new ExpenseException("Person not Found"));
        return mapToDto(expense);
    }

    private ExpenseDTO mapToDto(Expense expense) {
        ExpenseDTO expenseDTO = new ExpenseDTO();
        expenseDTO.setExpenseId(expense.getExpenseId());
        expenseDTO.setTitle(expense.getTitle());
        expenseDTO.setDescription(expense.getDescription());
        expenseDTO.setPrice(expense.getPrice());
        expenseDTO.setPersonId(expense.getPersonId());
        return expenseDTO;
    }

    private Expense dtoToMap(ExpenseDTO expenseDto) {
        Expense expense = new Expense();
        expense.setExpenseId(expenseDto.getExpenseId());
        expense.setPersonId(expenseDto.getPersonId());
        expense.setDescription(expenseDto.getDescription());
        expense.setTitle(expenseDto.getTitle());
        expense.setPrice(expenseDto.getPrice());
        return expense;
    }
}
