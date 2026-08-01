package com.example.smartexpensetracker.controller;

import com.example.smartexpensetracker.model.Expense;
import com.example.smartexpensetracker.service.ExpenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void addExpense_InvalidInput_ReturnsBadRequest() throws Exception {
        Expense invalidInput = Expense.builder()
                .title("") // Blank title should fail validation
                .amount(new BigDecimal("-10.00")) // Negative amount should fail
                .build();

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExpenses_ReturnsList() throws Exception {
        when(expenseService.getAllExpenses()).thenReturn(Arrays.asList(new Expense(), new Expense()));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
