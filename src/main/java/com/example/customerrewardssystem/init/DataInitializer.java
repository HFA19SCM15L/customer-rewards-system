package com.example.customerrewardssystem.init;

import com.example.customerrewardssystem.model.Customer;
import com.example.customerrewardssystem.model.Transaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.customerrewardssystem.repository.CustomerRepository;
import com.example.customerrewardssystem.repository.TransactionRepository;

import java.time.LocalDate;

/**
 * Data initializer to pre-populate the database with sample data.
 * Implements CommandLineRunner to run the initialization code at application startup.
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Constructor to autowire the Customer and Transaction repositories.
     */
    public DataInitializer(CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * Method executed at application startup to initialize data.
     */
    @Override
    public void run(String... args) throws Exception {
        // Create and save sample customer data
        Customer alice = new Customer(null, "Alice", "alice@example.com");
        Customer bob = new Customer(null, "Bob", "bob@example.com");
        customerRepository.save(alice);
        customerRepository.save(bob);

        // Initialize the current date
        LocalDate now = LocalDate.now();

        // Create and save transactions for Alice over a three-month period
        transactionRepository.save(new Transaction(null, now.minusMonths(2), 120.0, alice));
        transactionRepository.save(new Transaction(null, now.minusMonths(2), 75.0, alice));
        transactionRepository.save(new Transaction(null, now.minusMonths(2), 200.0, alice));
        transactionRepository.save(new Transaction(null, now.minusMonths(1), 140.0, alice));
        transactionRepository.save(new Transaction(null, now.minusMonths(1), 60.0, alice));
        transactionRepository.save(new Transaction(null, now, 220.0, alice));
        transactionRepository.save(new Transaction(null, now, 50.0, alice));

        // Create and save transactions for Bob over a three-month period
        transactionRepository.save(new Transaction(null, now.minusMonths(2), 50.0, bob));
        transactionRepository.save(new Transaction(null, now.minusMonths(2), 130.0, bob));
        transactionRepository.save(new Transaction(null, now.minusMonths(2), 90.0, bob));
        transactionRepository.save(new Transaction(null, now.minusMonths(1), 110.0, bob));
        transactionRepository.save(new Transaction(null, now.minusMonths(1), 100.0, bob));
        transactionRepository.save(new Transaction(null, now.minusMonths(1), 150.0, bob));
        transactionRepository.save(new Transaction(null, now, 80.0, bob));
        transactionRepository.save(new Transaction(null, now, 190.0, bob));
        transactionRepository.save(new Transaction(null, now, 120.0, bob));
    }
}
