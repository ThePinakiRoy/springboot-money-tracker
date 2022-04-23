package com.codewithroy.resttester.expense.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = true)
    @Lob
    private String description;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "personId", referencedColumnName = "personId")
    private Long personId;
}
