package com.expensesharing.com.expensesharing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Total amount is mandatory")
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private SplitType splitType;

    @ElementCollection
    @CollectionTable(name = "expense_participants", joinColumns = @JoinColumn(name = "expense_id"))
    private List<Participant> participants;

    public @NotBlank(message = "Description is mandatory") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description is mandatory") String description) {
        this.description = description;
    }

    public @NotNull(message = "Total amount is mandatory") Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(@NotNull(message = "Total amount is mandatory") Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public SplitType getSplitType() {
        return splitType;
    }

    public void setSplitType(SplitType splitType) {
        this.splitType = splitType;
    }
}

