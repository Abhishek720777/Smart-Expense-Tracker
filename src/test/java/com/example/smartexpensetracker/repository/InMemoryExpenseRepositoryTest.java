package com.example.smartexpensetracker.repository;

import com.example.smartexpensetracker.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryExpenseRepositoryTest {

    private InMemoryExpenseRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryExpenseRepository();
    }

    @Test
    void save_AssignsIdAndStoresExpense() {
        Expense saved = repository.save(expense(null, "Lunch", "Food"));

        assertNotNull(saved.getId());
        assertEquals(saved, repository.findById(saved.getId()).orElseThrow());
    }

    @Test
    void findByCategory_ReturnsCaseInsensitiveMatches() {
        repository.save(expense(UUID.randomUUID(), "Lunch", "Food"));
        repository.save(expense(UUID.randomUUID(), "Bus ticket", "Transport"));
        repository.save(expense(UUID.randomUUID(), "Groceries", "food"));

        List<Expense> foodExpenses = repository.findByCategory("FOOD");

        assertEquals(2, foodExpenses.size());
        assertTrue(foodExpenses.stream().allMatch(expense -> expense.getCategory().equalsIgnoreCase("food")));
    }

    @Test
    void findByCategory_IgnoresExpensesWithNullCategory() {
        repository.save(expense(UUID.randomUUID(), "Mystery", null));
        repository.save(expense(UUID.randomUUID(), "Groceries", "Food"));

        assertDoesNotThrow(() -> repository.findByCategory("food"));
        assertEquals(1, repository.findByCategory("food").size());
    }

    @Test
    void deleteById_RemovesExistingExpenseOnly() {
        Expense saved = repository.save(expense(UUID.randomUUID(), "Taxi", "Transport"));

        assertTrue(repository.deleteById(saved.getId()));
        assertFalse(repository.findById(saved.getId()).isPresent());
        assertFalse(repository.deleteById(saved.getId()));
    }

    @Test
    void findByTitleContaining_ReturnsCaseInsensitivePartialMatches() {
        repository.save(expense(UUID.randomUUID(), "Coffee with client", "Food"));
        repository.save(expense(UUID.randomUUID(), "Office supplies", "Work"));

        List<Expense> matches = repository.findByTitleContaining("coffee");

        assertEquals(1, matches.size());
        assertEquals("Coffee with client", matches.getFirst().getTitle());
    }

    @Test
    void findByTitleContaining_IgnoresExpensesWithNullTitle() {
        repository.save(expense(UUID.randomUUID(), null, "Food"));
        repository.save(expense(UUID.randomUUID(), "Coffee", "Food"));

        assertDoesNotThrow(() -> repository.findByTitleContaining("coffee"));
        assertEquals(1, repository.findByTitleContaining("coffee").size());
    }

    private Expense expense(UUID id, String title, String category) {
        return Expense.builder()
                .id(id)
                .title(title)
                .amount(new BigDecimal("12.50"))
                .category(category)
                .date(LocalDate.of(2026, 8, 1))
                .build();
    }
}
