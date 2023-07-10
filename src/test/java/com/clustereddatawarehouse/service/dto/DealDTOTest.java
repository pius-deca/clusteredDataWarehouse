package com.clustereddatawarehouse.service.dto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DealDTO.class)
class DealDTOTest {
    @Test
    void testConstructor() {
        DealDTO actualDealDTO = new DealDTO();
        BigDecimal value = BigDecimal.valueOf(42L);
        actualDealDTO.setAmount(value);
        actualDealDTO.setFromCurrency("GBP");
        actualDealDTO.setToCurrency("GBP");
        actualDealDTO.setId(20L);
        assertSame(value, actualDealDTO.getAmount());
        assertNotEquals("NGN", actualDealDTO.getFromCurrency());
        assertNotEquals("NGN", actualDealDTO.getToCurrency());
        assertNotEquals(10L, actualDealDTO.getId());
    }
}

