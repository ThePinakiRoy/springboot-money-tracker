package com.codewithroy.resttester.expense.web.controller;

import com.codewithroy.resttester.expense.service.ExpenseService;
import com.codewithroy.resttester.expense.web.dto.ExpenseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
@AllArgsConstructor
@Slf4j
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseDTO> createExpense(@RequestBody ExpenseDTO expenseRequest) {
       return ResponseEntity.status(HttpStatus.CREATED)
               .body(expenseService.createPost(expenseRequest));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDTO>> getAllExpense() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(expenseService.getAll());
    }
    @PutMapping
    public ResponseEntity<ExpenseDTO> updateExpense(@RequestBody ExpenseDTO expenseRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.updateExpense(expenseRequest));
    }
    @GetMapping("{id}")
    public ResponseEntity<ExpenseDTO> getExpenseById(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(expenseService.getExpenseById(id));
    }

}
