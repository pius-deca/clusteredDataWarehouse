package com.clustereddatawarehouse.controller;

import com.clustereddatawarehouse.ClusteredDataWarehouseApplication;
import com.clustereddatawarehouse.model.Deal;
import com.clustereddatawarehouse.repository.DealRepository;
import com.clustereddatawarehouse.service.dto.DealDTO;
import com.clustereddatawarehouse.service.mapper.DealMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = ClusteredDataWarehouseApplication.class)
public class DealControllerTest {

    @Autowired
    DealRepository dealRepository;

    @Autowired
    DealMapper dealMapper;

    @Autowired
    private MockMvc restDealMockMvc;

    private Deal deal;

    private static final ObjectMapper mapper = createObjectMapper();


    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return mapper.writeValueAsBytes(object);
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    private final static BigDecimal DEFAULT_DEAL_AMOUNT = BigDecimal.valueOf(1000);
    private final static Currency DEFAULT_FROM = Currency.getInstance("NGN");
    private final static Currency DEFAULT_TO = Currency.getInstance("USD");

    @BeforeEach
    public void initTest() {
        deal = initiateDeal();
    }

    @AfterEach
    public void deleteTest() {
        dealRepository.deleteAll();
    }

    public static Deal initiateDeal() {
        Deal deal = new Deal();
        deal.setAmount(DEFAULT_DEAL_AMOUNT);
        deal.setFromCurrency(DEFAULT_FROM);
        deal.setToCurrency(DEFAULT_TO);
        return deal;
    }

    @Test
    @Transactional
    void createDeal() throws Exception {
        int databaseSizeBeforeCreate = dealRepository.findAll().size();

        // Create the Deal
        DealDTO dealDTO = dealMapper.toDto(deal);
        restDealMockMvc
            .perform(post("/v1/api/deals").contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(dealDTO)))
            .andExpect(status().isCreated());

        // Validate the Deal in the database
        List<Deal> dealList = dealRepository.findAll();
        assertThat(dealList).hasSize(databaseSizeBeforeCreate + 1);
        Deal testDeal = dealList.get(dealList.size() - 1);
        assertThat(testDeal.getAmount()).isEqualTo(DEFAULT_DEAL_AMOUNT);
        assertThat(testDeal.getFromCurrency()).isEqualTo(DEFAULT_FROM);
        assertThat(testDeal.getToCurrency()).isEqualTo(DEFAULT_TO);
    }

    @Test
    @Transactional
    void getAllDeals() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get all the deals
        restDealMockMvc
            .perform(get("/v1/api/deals"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.data.[*].amount").value(hasItem(DEFAULT_DEAL_AMOUNT.intValue())))
            .andExpect(jsonPath("$.data.[*].fromCurrency").value(hasItem(DEFAULT_FROM.getCurrencyCode())))
            .andExpect(jsonPath("$.data.[*].toCurrency").value(hasItem(DEFAULT_TO.getCurrencyCode())));
    }

    @Test
    @Transactional
    void getDealById() throws Exception {
        // Initialize the database
        dealRepository.saveAndFlush(deal);

        // Get deal by id
        restDealMockMvc
            .perform(get("/v1/api/deal/"+deal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.data.amount").value(DEFAULT_DEAL_AMOUNT.intValue()))
            .andExpect(jsonPath("$.data.fromCurrency").value(DEFAULT_FROM.getCurrencyCode()))
            .andExpect(jsonPath("$.data.toCurrency").value(DEFAULT_TO.getCurrencyCode()));
    }

}
