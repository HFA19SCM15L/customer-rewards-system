package com.example.customerrewardssystem.repository;

import com.example.customerrewardssystem.model.Customer;
import com.example.customerrewardssystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for Transaction entities.
 * Provides CRUD operations and custom query methods related to transactions.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Find transactions between two dates.
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);

    // Find transactions starting from a specific date or after.
    List<Transaction> findByDateAfter(LocalDate startDate);

    // Find transactions ending on a specific date or before.
    List<Transaction> findByDateBefore(LocalDate endDate);

    // Find transactions for a specific customer.
    List<Transaction> findByCustomer(Customer customer);

    // Find transactions for a customer between two dates.
    List<Transaction> findByCustomerAndDateBetween(Customer customer, LocalDate startDate, LocalDate endDate);

    // Find transactions for a customer starting from a specific date or after.
    List<Transaction> findByCustomerAndDateAfter(Customer customer, LocalDate startDate);

    // Find transactions for a customer ending on a specific date or before.
    List<Transaction> findByCustomerAndDateBefore(Customer customer, LocalDate endDate);

    // Find transactions for a customer identified by ID.
    List<Transaction> findByCustomerId(Long customerId);

    // Find transactions for a customer identified by ID between two dates.
    List<Transaction> findByCustomerIdAndDateBetween(Long customerId, LocalDate startDate, LocalDate endDate);

    // Find transactions for a customer identified by ID starting from a specific date or after.
    List<Transaction> findByCustomerIdAndDateAfter(Long customerId, LocalDate startDate);

    // Find transactions for a customer identified by ID ending on a specific date or before.
    List<Transaction> findByCustomerIdAndDateBefore(Long customerId, LocalDate endDate);

    // Query to calculate the sum of transaction amounts for a specific customer.
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.customer = :customer")
    Double sumAmountForCustomer(Customer customer);

    // Query to calculate the sum of transaction amounts for a customer in a specific date range.
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.customer = :customer AND t.date BETWEEN :startDate AND :endDate")
    Double sumMonthlyAmountForCustomer(Customer customer, LocalDate startDate, LocalDate endDate);
}