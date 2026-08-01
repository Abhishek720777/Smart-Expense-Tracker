package com.example.smartexpensetracker.repository;

import com.example.smartexpensetracker.model.Expense;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {
    Expense save(Expense expense);
    List<Expense> findAll();
    List<Expense> findByCategory(String category);
    Optional<Expense> findById(UUID id);
    boolean deleteById(UUID id);
}
