package com.codewithroy.resttester.expense.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseDTO {
    private Long expenseId;
    private String title;
    private String description;
    private double price;
    private Long personId;
    private String personName;
}
