package com.expensesharing.com.expensesharing.entity;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Embeddable
@AllArgsConstructor
public class Participant {
    @NotNull(message = "User ID is mandatory")
    private Long userId;
    private Double amount;
    private Double percentage;

    public Participant(long l, double v, double o) {
        this.userId = l;
        this.amount = v;
        this.percentage = o;
    }

    public Participant() {

    }


    public Long getUserId() {
        return userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
