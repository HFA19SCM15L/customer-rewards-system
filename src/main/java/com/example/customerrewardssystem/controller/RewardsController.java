package com.example.customerrewardssystem.controller;

import com.example.customerrewardssystem.model.CustomerRewardsSummary;
import com.example.customerrewardssystem.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.customerrewardssystem.service.RewardsService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {
    private final RewardsService rewardsService;

    // Constructor to autowire the RewardsService
    @Autowired
    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }

    /**
     * Endpoint to calculate rewards for a list of transactions.
     *
     * @param transactions List of transactions.
     * @return ResponseEntity containing a list of CustomerRewardsSummary.
     */
    @PostMapping("/calculate")
    public ResponseEntity<List<CustomerRewardsSummary>> calculateRewards(@RequestBody List<Transaction> transactions) {
        List<CustomerRewardsSummary> rewards = rewardsService.calculateRewardsPerCustomer(transactions);
        return ResponseEntity.ok(rewards);
    }

    /**
     * Endpoint to get the rewards for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return ResponseEntity with CustomerRewardsSummary or Not Found if the customer does not exist.
     */
    @GetMapping("/{customerId}/rewards")
    public ResponseEntity<CustomerRewardsSummary> getRewardsForCustomer(@PathVariable Long customerId) {
        CustomerRewardsSummary rewards = rewardsService.calculateRewardsForCustomer(customerId);
        if (rewards == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(rewards);
    }

    /**
     * Endpoint to calculate rewards for a specific customer between given dates.
     * If start or end date is not provided, defaults to the last three months.
     *
     * @param customerId The ID of the customer.
     * @param startDate  The start date for calculating rewards.
     * @param endDate    The end date for calculating rewards.
     * @return ResponseEntity containing CustomerRewardsSummary.
     */
    @GetMapping("/{customerId}/calculate")
    public ResponseEntity<CustomerRewardsSummary> calculateRewardsForCustomer(
            @PathVariable Long customerId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {
        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        CustomerRewardsSummary rewards = rewardsService.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
        return ResponseEntity.ok(rewards);
    }
}