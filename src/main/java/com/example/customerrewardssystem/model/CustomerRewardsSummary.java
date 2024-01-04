package com.example.customerrewardssystem.model;

import java.util.List;

// Represents the reward summary of a customer
public class CustomerRewardsSummary {
    private Long customerId;
    private String customerName;
    private List<MonthlyReward> monthlyRewards;
    private Integer totalRewards;

    public CustomerRewardsSummary(Long customerId, String customerName, List<MonthlyReward> monthlyRewards, Integer totalRewards) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.monthlyRewards = monthlyRewards;
        this.totalRewards = totalRewards;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<MonthlyReward> getMonthlyRewards() {
        return monthlyRewards;
    }

    public void setMonthlyRewards(List<MonthlyReward> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }

    public Integer getTotalRewards() {
        return totalRewards;
    }

    public void setTotalRewards(Integer totalRewards) {
        this.totalRewards = totalRewards;
    }
}
