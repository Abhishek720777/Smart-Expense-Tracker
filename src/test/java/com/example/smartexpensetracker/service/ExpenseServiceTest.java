package com.example.smartexpensetracker.service;

import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    private Expense expense1;
    private Expense expense2;

    @BeforeEach
    void setUp() {
        expense1 = Expense.builder()
                .id(UUID.randomUUID())
                .title("Groceries")
                .amount(new BigDecimal("50.00"))
                .category("Food")
                .date(LocalDate.now())
                .build();

        expense2 = Expense.builder()
                .id(UUID.randomUUID())
                .title("Uber")
                .amount(new BigDecimal("20.00"))
                .category("Transport")
                .date(LocalDate.now())
                .build();
    }

    @Test
    void addExpense() {
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense1);
        Expense saved = expenseService.addExpense(expense1);
        assertEquals(expense1.getTitle(), saved.getTitle());
    }

    @Test
    void getAllExpenses() {
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense1, expense2));
        List<Expense> expenses = expenseService.getAllExpenses();
        assertEquals(2, expenses.size());
    }

    @Test
    void getTotalExpenses() {
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense1, expense2));
        BigDecimal total = expenseService.getTotalExpenses();
        assertEquals(new BigDecimal("70.00"), total);
    }
}
