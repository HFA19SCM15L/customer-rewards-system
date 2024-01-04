package com.example.customerrewardssystem.service;

import com.example.customerrewardssystem.model.Customer;
import com.example.customerrewardssystem.model.CustomerRewardsSummary;
import com.example.customerrewardssystem.model.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for the rewards service.
 * Defines methods for calculating reward points and summaries for customers.
 */
public interface RewardsService {
    /**
     * Calculates reward points for a single transaction amount.
     *
     * @param amount The transaction amount.
     * @return The calculated reward points.
     */
    int calculatePointsForTransaction(double amount);

    /**
     * Calculates rewards for multiple customers based on a list of transactions.
     *
     * @param transactions The list of transactions.
     * @return A list of CustomerRewardsSummary, each summarizing the rewards for a customer.
     */
    List<CustomerRewardsSummary> calculateRewardsPerCustomer(List<Transaction> transactions);

    /**
     * Calculates rewards for a specific customer.
     *
     * @param customerId The ID of the customer.
     * @return CustomerRewardsSummary containing the rewards details for the specified customer.
     */
    CustomerRewardsSummary calculateRewardsForCustomer(Long customerId);

    /**
     * Finds and calculates rewards for a customer within a specified date range.
     *
     * @param customer  The customer object.
     * @param startDate The start date of the range.
     * @param endDate   The end date of the range.
     * @return CustomerRewardsSummary for the specified customer and date range.
     */
    CustomerRewardsSummary findByCustomerAndDateBetween(Customer customer, LocalDate startDate, LocalDate endDate);

    /**
     * Finds and calculates rewards for a customer (identified by ID) within a specified date range.
     *
     * @param customerId The ID of the customer.
     * @param startDate  The start date of the range.
     * @param endDate    The end date of the range.
     * @return CustomerRewardsSummary for the specified customer and date range.
     */
    CustomerRewardsSummary findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);
}
