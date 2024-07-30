package com.expensesharing.com.expensesharing.service;

import com.expensesharing.com.expensesharing.entity.Expense;
import com.expensesharing.com.expensesharing.entity.Participant;
import com.expensesharing.com.expensesharing.entity.User;
import com.expensesharing.com.expensesharing.repositories.ExpenseRepository;
import com.expensesharing.com.expensesharing.repositories.UserRepository;
import com.expensesharing.com.expensesharing.util.ExpenseSplitUtil;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseSplitUtil expenseSplitUtil;

    public Expense addExpense(Expense expense) {
        try {
            validateParticipants(expense.getParticipants());
            expenseSplitUtil.splitExpense(expense);
            return expenseRepository.save(expense);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add expense", e);
        }
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new ValidationException("Expense not found"));
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public List<Expense> getExpensesByUserId(Long userId) {
        return expenseRepository.findByParticipantsUserId(userId);
    }

    private void validateParticipants(List<Participant> participants) {
        for (Participant participant : participants) {
            userRepository.findById(participant.getUserId())
                    .orElseThrow(() -> new ValidationException("User not found"));
        }
    }


    // This is the method which return the data in json format where it will be easy for the frontend users to display it in UI..
    public List<Map<String, Object>> generateBalanceSheetData() {
        List<Expense> allExpenses = getAllExpenses();
        List<Map<String, Object>> balanceSheetData = new ArrayList<>();

        for (Expense expense : allExpenses) {
            for (Participant participant : expense.getParticipants()) {
                User user = userRepository.findById(participant.getUserId())
                        .orElseThrow(() -> new ValidationException("User not found"));

                Map<String, Object> balanceEntry = new HashMap<>();
                balanceEntry.put("userName", user.getName());
                balanceEntry.put("email", user.getEmail());
                balanceEntry.put("mobile", user.getMobile());
                balanceEntry.put("expenseDescription", expense.getDescription());
                balanceEntry.put("amount", participant.getAmount());

                balanceSheetData.add(balanceEntry);
            }
        }

        return balanceSheetData;
    }


    // This is the method which is used to generate the data and save in the balance sheet...
//    public String generateAndSaveBalanceSheet() {
//        List<Expense> allExpenses = getAllExpenses();
//        StringBuilder balanceSheet = new StringBuilder();
//
//        balanceSheet.append("User, Email, Mobile, Expense Description, Amount\n");
//
//        for (Expense expense : allExpenses) {
//            for (Participant participant : expense.getParticipants()) {
//                User user = userRepository.findById(participant.getUserId())
//                        .orElseThrow(() -> new ValidationException("User not found"));
//                balanceSheet.append(user.getName()).append(", ")
//                        .append(user.getEmail()).append(", ")
//                        .append(user.getMobile()).append(", ")
//                        .append(expense.getDescription()).append(", ")
//                        .append(participant.getAmount()).append("\n");
//            }
//        }
//
//        String filePath = "src/main/resources/balance-sheet.csv";
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)))) {
//            writer.write(balanceSheet.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to write balance sheet to file");
//        }
//
//        return filePath;
//    }



    // This is just for returning the data to the certain api as normal way


//    public String generateBalanceSheet() {
//        List<Expense> allExpenses = getAllExpenses();
//        StringBuilder balanceSheet = new StringBuilder();
//
//        balanceSheet.append("User, Email, Mobile, Expense Description, Amount\n");
//
//        for (Expense expense : allExpenses) {
//            for (Participant participant : expense.getParticipants()) {
//                User user = userRepository.findById(participant.getUserId())
//                        .orElseThrow(() -> new ValidationException("User not found"));
//                balanceSheet.append(user.getName()).append(", ")
//                        .append(user.getEmail()).append(", ")
//                        .append(user.getMobile()).append(", ")
//                        .append(expense.getDescription()).append(", ")
//                        .append(participant.getAmount()).append("\n");
//            }
//        }
//        return balanceSheet.toString();
//    }
}
