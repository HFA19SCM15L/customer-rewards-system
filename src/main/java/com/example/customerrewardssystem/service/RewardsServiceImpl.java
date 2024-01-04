package com.example.customerrewardssystem.service;

import com.example.customerrewardssystem.model.Customer;
import com.example.customerrewardssystem.model.CustomerRewardsSummary;
import com.example.customerrewardssystem.model.MonthlyReward;
import com.example.customerrewardssystem.model.Transaction;
import com.example.customerrewardssystem.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.customerrewardssystem.repository.TransactionRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardsServiceImpl implements RewardsService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    /**
     * Constructor for autowiring the required repositories.
     */
    @Autowired
    public RewardsServiceImpl(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Calculates reward points based on a transaction amount.
     */
    @Override
    public int calculatePointsForTransaction(double amount) {
        if (amount <= 50) {
            return 0;
        } else if (amount <= 100) {
            return (int) (amount - 50);
        } else {
            return (int) (2 * (amount - 100) + 50);
        }
    }

    /**
     * Internal method to calculate rewards based on a list of transactions for a specific customer.
     */
    private CustomerRewardsSummary calculateRewardsWithTransactions(Customer customer, List<Transaction> transactions) {
        Map<String, Integer> monthlyRewardsMap = new HashMap<>();
        int totalPoints = 0;

        for (Transaction transaction : transactions) {
            YearMonth yearMonth = YearMonth.from(transaction.getDate());
            int points = calculatePointsForTransaction(transaction.getAmount());

            monthlyRewardsMap.merge(yearMonth.toString(), points, Integer::sum);
            totalPoints += points;
        }

        List<MonthlyReward> monthlyRewards = monthlyRewardsMap.entrySet().stream()
                .map(entry -> new MonthlyReward(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new CustomerRewardsSummary(customer.getId(), customer.getName(), monthlyRewards, totalPoints);
    }

    /**
     * Calculates rewards for each customer based on a list of transactions.
     * The rewards are calculated and summarized per customer.
     */
    @Override
    public List<CustomerRewardsSummary> calculateRewardsPerCustomer(List<Transaction> transactions) {
        Map<Long, List<Transaction>> transactionsByCustomer = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getCustomer().getId()));

        List<CustomerRewardsSummary> rewardsSummaries = new ArrayList<>();
        for (Map.Entry<Long, List<Transaction>> entry : transactionsByCustomer.entrySet()) {
            List<Transaction> customerTransactions = entry.getValue();
            Customer customer = customerTransactions.get(0).getCustomer();

            CustomerRewardsSummary summary = calculateRewardsWithTransactions(customer, customerTransactions);
            rewardsSummaries.add(summary);
        }

        return rewardsSummaries;
    }

    /**
     * Retrieves and calculates rewards for a specific customer.
     * Throws an exception if the customer is not found.
     */
    @Override
    public CustomerRewardsSummary calculateRewardsForCustomer(Long customerId) {
        // assume DAO only retrieve transactions from the most recent three-month period
        // otherwise use explicitly query to fetch relevant three-month transactions, such as findByCustomerAndDateBetween()
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        Customer customer = customerRepository.findCustomerById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        return calculateRewardsWithTransactions(customer, transactions);
    }

    /**
     * Calculates rewards for a customer within a specified date range.
     */
    @Override
    public CustomerRewardsSummary findByCustomerAndDateBetween(Customer customer, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByCustomerAndDateBetween(customer, startDate, endDate);
        return calculateRewardsWithTransactions(customer, transactions);
    }

    /**
     * Calculates rewards for a customer (identified by ID) within a specified date range.
     */
    @Override
    public CustomerRewardsSummary findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate) {
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
        Customer customer = customerRepository.findCustomerById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + customerId));
        return calculateRewardsWithTransactions(customer, transactions);
    }

}