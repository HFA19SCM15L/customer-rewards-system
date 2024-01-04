package com.example.customerrewardssystem.controller;

import com.example.customerrewardssystem.model.CustomerRewardsSummary;
import com.example.customerrewardssystem.model.MonthlyReward;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.customerrewardssystem.service.RewardsService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardsController.class)
public class RewardsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardsService rewardsService;

    /**
     * Test the POST /calculate endpoint to ensure it correctly processes a list of transactions
     * and returns a list of CustomerRewardsSummary objects. This test checks if the endpoint
     * correctly converts the list of transactions into rewards summaries for each customer.
     */
    @Test
    void testCalculateRewardsEndpoint() throws Exception {
        // Creating mock CustomerRewardsSummary data for two customers, Alice and Bob
        CustomerRewardsSummary summaryAlice = new CustomerRewardsSummary(1L, "Alice",
                Arrays.asList(
                        new MonthlyReward("2023-01", 150),
                        new MonthlyReward("2023-02", 120),
                        new MonthlyReward("2023-03", 90)
                ),
                360);

        CustomerRewardsSummary summaryBob = new CustomerRewardsSummary(2L, "Bob",
                Arrays.asList(
                        new MonthlyReward("2023-01", 90),
                        new MonthlyReward("2023-02", 100),
                        new MonthlyReward("2023-03", 110)
                ),
                300);

        // Setting up the RewardsService to return the mock data when called
        List<CustomerRewardsSummary> mockRewardsData = Arrays.asList(summaryAlice, summaryBob);
        when(rewardsService.calculateRewardsPerCustomer(anyList())).thenReturn(mockRewardsData);

        // JSON content representing a list of transactions
        String jsonContent = "[{\"id\":1,\"date\":\"2023-01-01\",\"amount\":120.0,\"customer\":{\"id\":1,\"name\":\"Alice\",\"email\":\"alice@example.com\"}},"
                + "{\"id\":2,\"date\":\"2023-01-02\",\"amount\":100.0,\"customer\":{\"id\":2,\"name\":\"Bob\",\"email\":\"bob@example.com\"}}]";

        // Perform a POST request with the mock JSON content and verify the response
        // Asserting that the response contains the correct data for both Alice and Bob
        mockMvc.perform(post("/api/rewards/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andDo(print())
                .andExpect(status().isOk())
                // Check for Alice
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].customerName").value("Alice"))
                .andExpect(jsonPath("$[0].monthlyRewards[?(@.month=='2023-01')].amount").value(150))
                .andExpect(jsonPath("$[0].monthlyRewards[?(@.month=='2023-02')].amount").value(120))
                .andExpect(jsonPath("$[0].monthlyRewards[?(@.month=='2023-03')].amount").value(90))
                .andExpect(jsonPath("$[0].totalRewards").value(360))
                // Check for Bob
                .andExpect(jsonPath("$[1].customerId").value(2L))
                .andExpect(jsonPath("$[1].customerName").value("Bob"))
                .andExpect(jsonPath("$[1].monthlyRewards[?(@.month=='2023-01')].amount").value(90))
                .andExpect(jsonPath("$[1].monthlyRewards[?(@.month=='2023-02')].amount").value(100))
                .andExpect(jsonPath("$[1].monthlyRewards[?(@.month=='2023-03')].amount").value(110))
                .andExpect(jsonPath("$[1].totalRewards").value(300));
    }

    /**
     * Test the GET /{customerId}/rewards endpoint to verify if it correctly retrieves
     * and returns the rewards summary for a specific customer. This test focuses on validating
     * whether the endpoint can handle a request for a specific customer's rewards data
     * and return the correct response.
     */
    @Test
    void testGetRewardsForCustomerEndpoint() throws Exception {
        // Creating mock CustomerRewardsSummary data for a specific customer, Alice
        Long customerId = 1L;
        CustomerRewardsSummary mockSummary = new CustomerRewardsSummary(customerId, "Alice",
                Arrays.asList(
                        new MonthlyReward("2023-01", 150),
                        new MonthlyReward("2023-02", 120),
                        new MonthlyReward("2023-03", 90)
                ),
                360);

        // Mocking the RewardsService's response for a specific customer ID
        when(rewardsService.calculateRewardsForCustomer(customerId)).thenReturn(mockSummary);

        // Perform a GET request for the specific customer's rewards and verify the response
        // Asserting that the response correctly reflects Alice's rewards data
        mockMvc.perform(get("/api/rewards/" + customerId + "/rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.totalRewards").value(360))
                .andExpect(jsonPath("$.monthlyRewards[0].month").value("2023-01"))
                .andExpect(jsonPath("$.monthlyRewards[0].amount").value(150))
                .andExpect(jsonPath("$.monthlyRewards[1].month").value("2023-02"))
                .andExpect(jsonPath("$.monthlyRewards[1].amount").value(120))
                .andExpect(jsonPath("$.monthlyRewards[2].month").value("2023-03"))
                .andExpect(jsonPath("$.monthlyRewards[2].amount").value(90));
    }

    /**
     * Test the GET /{customerId}/calculate endpoint to ensure it correctly calculates
     * and returns the rewards for a specific customer over a given date range. This test checks
     * if the endpoint processes parameters like startDate and endDate correctly and
     * returns the appropriate rewards summary.
     */
    @Test
    void testCalculateRewardsForCustomerEndpoint() throws Exception {
        // Creating mock CustomerRewardsSummary data for a specific customer, Alice, over a date range
        Long customerId = 1L;
        LocalDate startDate = LocalDate.now().minusMonths(3);
        LocalDate endDate = LocalDate.now();
        CustomerRewardsSummary mockSummary = new CustomerRewardsSummary(
                customerId,
                "Alice",
                Arrays.asList(
                        new MonthlyReward("2023-01", 150),
                        new MonthlyReward("2023-02", 120),
                        new MonthlyReward("2023-03", 90)
                ),
                360
        );

        // Mocking the RewardsService's response for a specific customer ID and date range
        when(rewardsService.findByCustomerIdAndDateBetween(customerId, startDate, endDate)).thenReturn(mockSummary);

        // Perform a GET request with date range parameters and verify the response
        // Asserting that the response matches the expected rewards summary for the given date range
        mockMvc.perform(get("/api/rewards/" + customerId + "/calculate")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(customerId))
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.totalRewards").value(360))
                .andExpect(jsonPath("$.monthlyRewards[0].month").value("2023-01"))
                .andExpect(jsonPath("$.monthlyRewards[0].amount").value(150))
                .andExpect(jsonPath("$.monthlyRewards[1].month").value("2023-02"))
                .andExpect(jsonPath("$.monthlyRewards[1].amount").value(120))
                .andExpect(jsonPath("$.monthlyRewards[2].month").value("2023-03"))
                .andExpect(jsonPath("$.monthlyRewards[2].amount").value(90));
    }
}