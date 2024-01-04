
# Customer Rewards System

## Description

The Customer Rewards System is a Spring Boot application designed for retailers to effectively manage a rewards program. It allocates points to customers for each transaction they make, using a tiered point system that varies based on different spending thresholds.

## Features

- **Calculate Reward Points**: Automatically assigns points for each transaction based on the purchase amount.
- **Monthly Tracking**: Enables month-to-month monitoring of customer rewards.
- **Rewards Summary**: Provides a detailed summary of the total rewards accrued by each customer.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java JDK 11 or higher
- Maven 3.6 or higher

## Installation

To set up the Customer Rewards System on your local machine, follow these steps:

1. **Clone the Repository**:
   Open your terminal and clone the repository with the following command:
    ```sh
    git clone https://github.com/HFA19SCM15L/customer-rewards-system.git
    ```

2. **Navigate to the Project Directory**:
   Change to the project directory:
    ```sh
    cd customer-rewards-system
    ```

3. **Build the Project Using Maven**:
   Build the project using Maven:
    ```sh
    mvn clean install
    ```

## How to Use

Start the application by running the following command in your terminal:

```sh
mvn spring-boot:run
```

Once started, the system will be accessible at `http://localhost:8080`.

## Endpoints

- **POST `/api/rewards/calculate`**: Calculate rewards for a batch of transactions.
- **GET `/api/rewards/{customerId}/rewards`**: Retrieve the reward summary for a specific customer.
- **GET `/api/rewards/{customerId}/calculate`**: Calculate rewards for a specific customer within a specified date range.

## Running Tests
Execute the following command to run tests:
```sh
mvn test
```