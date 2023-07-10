package com.clustereddatawarehouse.service.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode
public class DealDTO {
    private Long id;

    private String fromCurrency;

    private String toCurrency;

    private BigDecimal amount;

    private Instant createdAt;
}
