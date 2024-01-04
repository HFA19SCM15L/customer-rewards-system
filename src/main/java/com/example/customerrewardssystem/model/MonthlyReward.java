package com.example.customerrewardssystem.model;

// Represents the rewards earned by a customer for a specific month
public class MonthlyReward {
    private String month;
    private Integer amount;

    public MonthlyReward(String month, Integer amount) {
        this.month = month;
        this.amount = amount;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
