package com.expensesharing.com.expensesharing.util;

import com.expensesharing.com.expensesharing.entity.Expense;
import com.expensesharing.com.expensesharing.entity.Participant;
import com.expensesharing.com.expensesharing.entity.SplitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseSplitUtilTest {

    private ExpenseSplitUtil expenseSplitUtil;

    @BeforeEach
    void setUp() {
        expenseSplitUtil = new ExpenseSplitUtil();
    }

    @Test
    void splitExpense_whenSplitTypeIsEqual_shouldSplitEqually() throws Exception {
        Expense expense = new Expense();
        expense.setSplitType(SplitType.EQUAL);
        expense.setTotalAmount(100.0);

        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant());
        participants.add(new Participant());
        expense.setParticipants(participants);

        expenseSplitUtil.splitExpense(expense);

        assertEquals(50.0, participants.get(0).getAmount());
        assertEquals(50.0, participants.get(1).getAmount());
        assertNull(participants.get(0).getPercentage());
        assertNull(participants.get(1).getPercentage());
    }

    @Test
    void splitExpense_whenSplitTypeIsEqual_noParticipants_shouldNotSplit() throws Exception {
        Expense expense = new Expense();
        expense.setSplitType(SplitType.EQUAL);
        expense.setTotalAmount(100.0);
        expense.setParticipants(Collections.emptyList());

        expenseSplitUtil.splitExpense(expense);

    }

    @Test
    void splitExpense_whenSplitTypeIsPercentage_shouldSplitByPercentage() throws Exception {
        Expense expense = new Expense();
        expense.setSplitType(SplitType.PERCENTAGE);
        expense.setTotalAmount(100.0);

        List<Participant> participants = new ArrayList<>();
        Participant p1 = new Participant();
        p1.setPercentage(50.0);
        Participant p2 = new Participant();
        p2.setPercentage(50.0);
        participants.add(p1);
        participants.add(p2);
        expense.setParticipants(participants);

        expenseSplitUtil.splitExpense(expense);

        assertEquals(50.0, p1.getAmount());
        assertEquals(50.0, p2.getAmount());
    }

    @Test
    void splitExpense_whenSplitTypeIsPercentage_withZeroOrNullPercentage_shouldSkip() throws Exception {
        Expense expense = new Expense();
        expense.setSplitType(SplitType.PERCENTAGE);
        expense.setTotalAmount(100.0);

        List<Participant> participants = new ArrayList<>();
        Participant p1 = new Participant();
        p1.setPercentage(null);
        Participant p2 = new Participant();
        p2.setPercentage(0.0);
        participants.add(p1);
        participants.add(p2);
        expense.setParticipants(participants);

        expenseSplitUtil.splitExpense(expense);

        assertNull(p1.getAmount());
        assertNull(p2.getAmount());
    }

    @Test
    void splitExpense_whenSplitTypeIsInvalid_shouldThrowIllegalArgumentException() {
        Expense expense = new Expense();
        expense.setSplitType(null);
        expense.setTotalAmount(100.0);

        assertThrows(Exception.class, () -> {
            expenseSplitUtil.splitExpense(expense);
        });

    }


}
