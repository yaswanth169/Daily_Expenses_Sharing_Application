package com.expensesharing.com.expensesharing.controller;

import com.expensesharing.com.expensesharing.entity.Expense;
import com.expensesharing.com.expensesharing.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController expenseController;

    @Mock
    private ExpenseService expenseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddExpense() {
        Expense expense = new Expense();
        when(expenseService.addExpense(any(Expense.class))).thenReturn(expense);

        ResponseEntity<Expense> response = expenseController.addExpense(expense);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    public void testGetExpenseById() {
        Expense expense = new Expense();
        when(expenseService.getExpenseById(1L)).thenReturn(expense);

        ResponseEntity<Expense> response = expenseController.getExpenseById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    public void testGetAllExpenses() {
        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        when(expenseService.getAllExpenses()).thenReturn(expenses);

        ResponseEntity<List<Expense>> response = expenseController.getAllExpenses();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenses, response.getBody());
    }

    @Test
    public void testGetExpensesByUserId() {
        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        when(expenseService.getExpensesByUserId(1L)).thenReturn(expenses);

        ResponseEntity<List<Expense>> response = expenseController.getExpensesByUserId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expenses, response.getBody());
    }

    @Test
    public void testDownloadBalanceSheet() {
        List<Map<String, Object>> balanceSheetData = Arrays.asList(
                Map.of("userName", "John", "email", "test@example.com", "mobile", "1234567890", "expenseDescription", "Lunch", "amount", 50.0),
                Map.of("userName", "Jane", "email", "test@example.com", "mobile", "0987654321", "expenseDescription", "Dinner", "amount", 30.0)
        );
        when(expenseService.generateBalanceSheetData()).thenReturn(balanceSheetData);

        ResponseEntity<List<Map<String, Object>>> response = expenseController.downloadBalanceSheet();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(balanceSheetData, response.getBody());
    }
}
