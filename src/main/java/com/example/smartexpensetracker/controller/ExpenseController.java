package com.example.smartexpensetracker.controller;

import com.example.smartexpensetracker.exception.ResourceNotFoundException;
import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Expenses", description = "Manage personal expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @Operation(summary = "Add a new expense", description = "Creates a new expense. All fields except id are required.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Expense created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed - check error message for details")
    })
    @PostMapping
    public ResponseEntity<Expense> addExpense(@Valid @RequestBody Expense expense) {
        return new ResponseEntity<>(expenseService.addExpense(expense), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get expenses",
            description = "Returns all expenses. Optionally filter by category or search by title keyword. " +
                    "If both are provided, search takes priority."
    )
    @ApiResponse(responseCode = "200", description = "List of expenses")
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            @Parameter(description = "Filter by category (case-insensitive)")
            @RequestParam(required = false) String category,
            @Parameter(description = "Search by title keyword (case-insensitive, partial match)")
            @RequestParam(required = false) String search) {

        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(expenseService.searchExpenses(search));
        }
        if (category != null && !category.trim().isEmpty()) {
            return ResponseEntity.ok(expenseService.getExpensesByCategory(category));
        }
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @Operation(
            summary = "Get total expenses",
            description = "Returns the sum of all expense amounts. Optionally scope to a specific category."
    )
    @ApiResponse(responseCode = "200", description = "Total amount as JSON: { \"total\": 123.45 }")
    @GetMapping("/total")
    public ResponseEntity<Map<String, BigDecimal>> getTotalExpenses(
            @Parameter(description = "Scope total to a specific category")
            @RequestParam(required = false) String category) {

        BigDecimal total = (category != null && !category.trim().isEmpty())
                ? expenseService.getTotalExpensesByCategory(category)
                : expenseService.getTotalExpenses();

        return ResponseEntity.ok(Map.of("total", total));
    }

    @Operation(summary = "Delete an expense", description = "Deletes an expense by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Expense deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Expense not found"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(
            @Parameter(description = "UUID of the expense to delete")
            @PathVariable UUID id) {

        if (!expenseService.deleteExpense(id)) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        return ResponseEntity.noContent().build();
    }
}
