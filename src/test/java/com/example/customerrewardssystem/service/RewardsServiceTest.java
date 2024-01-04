package com.example.customerrewardssystem.service;

import com.example.customerrewardssystem.model.Customer;
import com.example.customerrewardssystem.model.CustomerRewardsSummary;
import com.example.customerrewardssystem.model.Transaction;
import com.example.customerrewardssystem.model.MonthlyReward;
import com.example.customerrewardssystem.repository.CustomerRepository;
import com.example.customerrewardssystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class RewardsServiceTest {
    private RewardsServiceImpl rewardsService;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private CustomerRepository customerRepository;

    /**
     * Set up the testing environment before each test.
     * Initializes mocks and creates an instance of RewardsServiceImpl with these mocks.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rewardsService = new RewardsServiceImpl(transactionRepository, customerRepository);
    }

    /**
     * Test the method to calculate rewards for multiple customers based on a list of transactions.
     * This test verifies that the method correctly processes a list of transactions
     * and returns a summary of rewards for each customer.
     */
    @Test
    void testCalculateRewardsPerCustomer() {
        // Setting up mock customers and transactions
        Customer alice = new Customer(1L, "Alice", "alice@example.com");
        Customer bob = new Customer(2L, "Bob", "bob@example.com");
        Customer carl = new Customer(3L, "Carl", "carl@example.com");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1L, LocalDate.of(2024, 1, 10), 120.0, alice));
        transactions.add(new Transaction(2L, LocalDate.of(2024, 2, 15), 100.0, alice));
        transactions.add(new Transaction(3L, LocalDate.of(2024, 2, 5), 150.0, bob));
        transactions.add(new Transaction(4L, LocalDate.of(2024, 3, 1), 50.0, carl));

        // Executing the method under test
        List<CustomerRewardsSummary> rewardsSummaries = rewardsService.calculateRewardsPerCustomer(transactions);

        // Find the reward summaries for Alice and Bob
        CustomerRewardsSummary aliceSummary = rewardsSummaries.stream()
                .filter(summary -> summary.getCustomerId().equals(alice.getId()))
                .findFirst()
                .orElse(null);

        CustomerRewardsSummary bobSummary = rewardsSummaries.stream()
                .filter(summary -> summary.getCustomerId().equals(bob.getId()))
                .findFirst()
                .orElse(null);

        CustomerRewardsSummary carlSummary = rewardsSummaries.stream()
                .filter(summary -> summary.getCustomerId().equals(carl.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(aliceSummary);
        assertNotNull(bobSummary);

        // Verifying the results for each customer, including reward calculations and customer details
        // Assert the results for Alice
        assertEquals(alice.getId(), aliceSummary.getCustomerId());
        assertEquals(alice.getName(), aliceSummary.getCustomerName());
        assertEquals(90, aliceSummary.getMonthlyRewards().stream()
                .filter(reward -> reward.getMonth().equals("2024-01"))
                .findFirst()
                .map(MonthlyReward::getAmount)
                .orElse(0));
        assertEquals(50, aliceSummary.getMonthlyRewards().stream()
                .filter(reward -> reward.getMonth().equals("2024-02"))
                .findFirst()
                .map(MonthlyReward::getAmount)
                .orElse(0));
        assertEquals(140, aliceSummary.getTotalRewards());

        // Assert the results for Bob
        assertEquals(bob.getId(), bobSummary.getCustomerId());
        assertEquals(bob.getName(), bobSummary.getCustomerName());
        assertEquals(150, bobSummary.getMonthlyRewards().stream()
                .filter(reward -> reward.getMonth().equals("2024-02"))
                .findFirst()
                .map(MonthlyReward::getAmount)
                .orElse(0));
        assertEquals(150, bobSummary.getTotalRewards());

        // Assert the results for Carl
        assertEquals(carl.getId(), carlSummary.getCustomerId());
        assertEquals(carl.getName(), carlSummary.getCustomerName());
        assertEquals(0, carlSummary.getMonthlyRewards().stream()
                .filter(reward -> reward.getMonth().equals("2024-03"))
                .findFirst()
                .map(MonthlyReward::getAmount)
                .orElse(0));
        assertEquals(0, carlSummary.getTotalRewards());
    }

    /**
     * Test the method to calculate rewards for a specific customer.
     * This test checks if the method correctly fetches transactions for a given customer
     * and returns an accurate summary of their rewards.
     */
    @Test
    void testCalculateRewardsForCustomer() {
        // Setting up a mock customer and their transactions
        Long customerId = 1L;
        Customer alice = new Customer(customerId, "Alice", "alice@example.com");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, LocalDate.of(2024, 1, 10), 120.0, alice),
                new Transaction(2L, LocalDate.of(2024, 2, 15), 100.0, alice)
        );

        // Mocking repository responses and executing the method under test
        when(transactionRepository.findByCustomerId(customerId)).thenReturn(transactions);
        when(customerRepository.findCustomerById(customerId)).thenReturn(Optional.of(alice));

        CustomerRewardsSummary summary = rewardsService.calculateRewardsForCustomer(customerId);
        // Verifying that the returned summary matches the expected rewards for the customer
        assertNotNull(summary);
        assertEquals(customerId, summary.getCustomerId());
        assertEquals(alice.getName(), summary.getCustomerName());
        assertEquals(90, summary.getMonthlyRewards().stream()
                .filter(reward -> reward.getMonth().equals("2024-01"))
                .findFirst()
                .map(MonthlyReward::getAmount)
                .orElse(0));
        assertEquals(140, summary.getTotalRewards());
    }

    /**
     * Test the method to find and calculate rewards for a customer within a specific date range.
     * This test ensures that the method correctly filters transactions by date
     * and calculates rewards within this range.
     */
    @Test
    void testFindByCustomerAndDateBetween() {
        // Setting up a mock customer and transactions within a date range
        Customer bob = new Customer(2L, "Bob", "bob@example.com");
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 2, 1);
        List<Transaction> transactions = Arrays.asList(
                new Transaction(3L, LocalDate.of(2024, 1, 20), 150.0, bob),
                new Transaction(4L, LocalDate.of(2024, 1, 21), 150.0, bob)
        );

        // Mocking repository responses and executing the method under test
        when(transactionRepository.findByCustomerAndDateBetween(bob, startDate, endDate)).thenReturn(transactions);

        CustomerRewardsSummary summary = rewardsService.findByCustomerAndDateBetween(bob, startDate, endDate);
        // Verifying the rewards summary for the specified date range
        assertNotNull(summary);
        assertEquals(bob.getId(), summary.getCustomerId());
        assertEquals(300, summary.getMonthlyRewards().stream()
                .filter(reward -> reward.getMonth().equals("2024-01"))
                .findFirst()
                .map(MonthlyReward::getAmount)
                .orElse(0));
        assertEquals(300, summary.getTotalRewards());
    }

    /**
     * Test the method to find and calculate rewards for a customer (identified by ID) within a specific date range.
     * Similar to the previous test, this test ensures accurate reward calculation based on customer ID and date range.
     */
    @Test
    void testFindByCustomerIdAndDateBetween() {
        // Setting up a mock customer identified by ID and their transactions within a date range
        Long customerId = 3L;
        Customer carl = new Customer(customerId, "Carl", "carl@example.com");
        LocalDate startDate = LocalDate.of(2024, 2, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 1);
        List<Transaction> transactions = Collections.singletonList(
                new Transaction(4L, LocalDate.of(2024, 2, 15), 200.0, carl)
        );

        // Mocking repository responses and executing the method under test
        when(transactionRepository.findByCustomerIdAndDateBetween(customerId, startDate, endDate)).thenReturn(transactions);
        when(customerRepository.findCustomerById(customerId)).thenReturn(Optional.of(carl));

        CustomerRewardsSummary summary = rewardsService.findByCustomerIdAndDateBetween(customerId, startDate, endDate);
        // Verifying the rewards summary is correct for the specified customer and date range
        assertNotNull(summary);
        assertEquals(customerId, summary.getCustomerId());
        assertEquals(250, summary.getMonthlyRewards().get(0).getAmount());
        assertEquals(250, summary.getTotalRewards());
    }
}
