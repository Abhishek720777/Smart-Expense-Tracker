package com.example.smartexpensetracker.controller;

import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<Expense> addExpense(@Valid @RequestBody Expense expense) {
        Expense createdExpense = expenseService.addExpense(expense);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(required = false) String category) {
        if (category != null && !category.trim().isEmpty()) {
            return ResponseEntity.ok(expenseService.getExpensesByCategory(category));
        }
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, BigDecimal>> getTotalExpenses(
            @RequestParam(required = false) String category) {
        BigDecimal total;
        if (category != null && !category.trim().isEmpty()) {
            total = expenseService.getTotalExpensesByCategory(category);
        } else {
            total = expenseService.getTotalExpenses();
        }
        return ResponseEntity.ok(Map.of("total", total));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable UUID id) {
        boolean deleted = expenseService.deleteExpense(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
