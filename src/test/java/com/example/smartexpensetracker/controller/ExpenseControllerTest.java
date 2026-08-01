package com.example.smartexpensetracker.controller;

import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseService expenseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addExpense_ValidInput_ReturnsCreated() throws Exception {
        Expense input = Expense.builder()
                .title("Lunch")
                .amount(new BigDecimal("15.50"))
                .category("Food")
                .date(LocalDate.now())
                .build();

        Expense saved = Expense.builder()
                .id(UUID.randomUUID())
                .title("Lunch")
                .amount(new BigDecimal("15.50"))
                .category("Food")
                .date(LocalDate.now())
                .build();

        when(expenseService.addExpense(any(Expense.class))).thenReturn(saved);

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Lunch"));
    }

    @Test
    void addExpense_InvalidInput_ReturnsBadRequestBody() throws Exception {
        Expense invalidInput = Expense.builder()
                .title("")
                .amount(new BigDecimal("-10.00"))
                .build();

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation Failed"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void addExpense_InvalidDateFormat_ReturnsHelpfulBadRequest() throws Exception {
        String invalidDateJson = """
                {
                  "title": "Groceries",
                  "amount": 45.50,
                  "category": "Food",
                  "date": "01-08-2026"
                }
                """;

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidDateJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Malformed JSON request or invalid field format. Dates must use yyyy-MM-dd."));
    }

    @Test
    void getExpenses_ReturnsList() throws Exception {
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(new Expense(), new Expense()));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getExpenses_WithSearch_ReturnsSearchResults() throws Exception {
        Expense coffee = Expense.builder()
                .id(UUID.randomUUID())
                .title("Coffee")
                .amount(new BigDecimal("4.50"))
                .category("Food")
                .date(LocalDate.of(2026, 8, 1))
                .build();

        when(expenseService.searchExpenses("coffee")).thenReturn(List.of(coffee));

        mockMvc.perform(get("/api/expenses").param("search", "coffee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Coffee"));
    }

    @Test
    void deleteExpense_InvalidUuid_ReturnsBadRequestBody() throws Exception {
        mockMvc.perform(delete("/api/expenses/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Invalid value 'not-a-uuid' for parameter 'id'. Expected type: UUID"));
    }

    @Test
    void deleteExpense_MissingExpense_ReturnsNotFoundBody() throws Exception {
        UUID missingId = UUID.randomUUID();
        when(expenseService.deleteExpense(missingId)).thenReturn(false);

        mockMvc.perform(delete("/api/expenses/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.message").value("Expense not found with id: " + missingId))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
