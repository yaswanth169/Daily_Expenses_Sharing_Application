package com.expensesharing.com.expensesharing.util;

import com.expensesharing.com.expensesharing.entity.Expense;
import com.expensesharing.com.expensesharing.entity.Participant;
import org.springframework.stereotype.Component;

@Component
public class ExpenseSplitUtil {

    public void splitExpense(Expense expense) throws Exception {
        try {
            switch (expense.getSplitType()) {
                case EQUAL:
                    splitEqual(expense);
                    break;
                case EXACT:
                    // Assuming amounts are already set in participants
                    break;
                case PERCENTAGE:
                    splitPercentage(expense);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported split type");
            }
        } catch (Exception e) {
            throw new Exception("Failed to split expense", e);
        }
    }

    private void splitEqual(Expense expense) {
        double totalAmount = expense.getTotalAmount();
        int participantsCount = expense.getParticipants().size();
        try {
            if (participantsCount == 0) {
                System.out.println("No participants found for splitting expense equally.");
                return;
            }

            double splitAmount = totalAmount / participantsCount;

            for (Participant participant : expense.getParticipants()) {
                participant.setAmount(splitAmount);
                participant.setPercentage(null);
            }
        } catch (ArithmeticException e) {
            System.out.println("An error occurred while splitting the expense.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
        }
    }

    private void splitPercentage(Expense expense) {
        double totalAmount = expense.getTotalAmount();

        for (Participant participant : expense.getParticipants()) {
            try {
                if (participant.getPercentage() != null && participant.getPercentage() > 0) {
                    participant.setAmount(totalAmount * (participant.getPercentage() / 100));
                } else {
                    continue;
                }
            } catch (ArithmeticException e) {
                System.out.println("Error calculating participant's share: " + e.getMessage());
                continue;
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
                continue;
            }
        }
    }
}
