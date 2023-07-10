package com.clustereddatawarehouse.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest(classes = Deal.class)
class DealTest {
    @Test
    void testConstructor() {
        Deal actualDeal = new Deal();
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        actualDeal.setAmount(valueOfResult);
        actualDeal.setFromCurrency(null);
        actualDeal.setId(1L);
        actualDeal.setToCurrency(null);
        assertSame(valueOfResult, actualDeal.getAmount());
        assertNull(actualDeal.getFromCurrency());
        assertNull(actualDeal.getToCurrency());
    }
}

