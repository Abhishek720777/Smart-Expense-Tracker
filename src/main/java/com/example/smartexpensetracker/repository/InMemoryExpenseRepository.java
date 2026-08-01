package com.example.smartexpensetracker.repository;

import com.example.smartexpensetracker.model.Expense;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryExpenseRepository implements ExpenseRepository {
    private final Map<UUID, Expense> expenses = new ConcurrentHashMap<>();

    @Override
    public Expense save(Expense expense) {
        if (expense.getId() == null) {
            expense.setId(UUID.randomUUID());
        }
        expenses.put(expense.getId(), expense);
        return expense;
    }

    @Override
    public List<Expense> findAll() {
        return new ArrayList<>(expenses.values());
    }

    @Override
    public List<Expense> findByCategory(String category) {
        return expenses.values().stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Expense> findByTitleContaining(String keyword) {
        return expenses.values().stream()
                .filter(e -> e.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return Optional.ofNullable(expenses.get(id));
    }

    @Override
    public boolean deleteById(UUID id) {
        return expenses.remove(id) != null;
    }
}
