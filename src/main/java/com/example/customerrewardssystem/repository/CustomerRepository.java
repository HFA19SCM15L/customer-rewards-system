package com.example.customerrewardssystem.repository;

import com.example.customerrewardssystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Customer entities.
 * Provides CRUD operations and custom query methods.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Find a customer by their email.
    Optional<Customer> findCustomerByEmail(String email);

    // Find a customer by their ID.
    Optional<Customer> findCustomerById(Long id);

    // Find customers by their name.
    List<Customer> findCustomerByName(String name);

    // Find customers whose names contain the specified fragment, case-insensitive.
    List<Customer> findCustomerByNameContainingIgnoreCase(String nameFragment);
}
