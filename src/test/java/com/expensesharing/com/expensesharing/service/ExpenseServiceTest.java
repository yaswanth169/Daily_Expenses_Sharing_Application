package com.expensesharing.com.expensesharing.service;

import com.expensesharing.com.expensesharing.entity.Expense;
import com.expensesharing.com.expensesharing.entity.Participant;
import com.expensesharing.com.expensesharing.entity.User;
import com.expensesharing.com.expensesharing.repositories.ExpenseRepository;
import com.expensesharing.com.expensesharing.repositories.UserRepository;
import com.expensesharing.com.expensesharing.util.ExpenseSplitUtil;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseSplitUtil expenseSplitUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddExpense_Success() throws Exception {
        Expense expense = new Expense();
        Participant participant = new Participant(1L, 100.0, 0.0);
        expense.setParticipants(Arrays.asList(participant));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        Expense result = expenseService.addExpense(expense);
        assertEquals(expense, result);
        verify(expenseSplitUtil, times(1)).splitExpense(expense);
        verify(expenseRepository, times(1)).save(expense);
    }

    @Test
    public void testAddExpense_UserNotFound() {
        Expense expense = new Expense();
        Participant participant = new Participant(1L, 100.0, 0.0);
        expense.setParticipants(Arrays.asList(participant));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            expenseService.addExpense(expense);
        });

        String expectedMessage = "Failed to add expense";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        verify(expenseRepository, times(0)).save(any(Expense.class));
    }

    @Test
    public void testGetExpenseById_Success() {
        Expense expense = new Expense();
        when(expenseRepository.findById(1L)).thenReturn(Optional.of(expense));

        Expense result = expenseService.getExpenseById(1L);
        assertEquals(expense, result);
    }

    @Test
    public void testGetExpenseById_NotFound() {
        when(expenseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ValidationException.class, () -> {
            expenseService.getExpenseById(1L);
        });

        String expectedMessage = "Expense not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetAllExpenses() {
        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        when(expenseRepository.findAll()).thenReturn(expenses);

        List<Expense> result = expenseService.getAllExpenses();
        assertEquals(expenses, result);
    }

    @Test
    public void testGetExpensesByUserId() {
        List<Expense> expenses = Arrays.asList(new Expense(), new Expense());
        when(expenseRepository.findByParticipantsUserId(1L)).thenReturn(expenses);

        List<Expense> result = expenseService.getExpensesByUserId(1L);
        assertEquals(expenses, result);
    }

    @Test
    public void testGenerateBalanceSheetData_Success() {
        Expense expense = new Expense();
        Participant participant = new Participant(1L, 100.0, 0.0);
        expense.setParticipants(Arrays.asList(participant));
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense));
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setMobile("1234567890");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<Map<String, Object>> result = expenseService.generateBalanceSheetData();
        assertEquals(1, result.size());
        Map<String, Object> balanceEntry = result.get(0);
        assertEquals("John Doe", balanceEntry.get("userName"));
        assertEquals("john.doe@example.com", balanceEntry.get("email"));
        assertEquals("1234567890", balanceEntry.get("mobile"));
        assertEquals(100.0, balanceEntry.get("amount"));
    }

    @Test
    public void testGenerateBalanceSheetData_UserNotFound() {
        Expense expense = new Expense();
        Participant participant = new Participant(1L, 100.0, 0.0);
        expense.setParticipants(Arrays.asList(participant));
        when(expenseRepository.findAll()).thenReturn(Arrays.asList(expense));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ValidationException.class, () -> {
            expenseService.generateBalanceSheetData();
        });

        String expectedMessage = "User not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
