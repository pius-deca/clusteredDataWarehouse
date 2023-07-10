package com.clustereddatawarehouse.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

@Entity
@Table(name = "deals")
@Getter
@Setter
@EqualsAndHashCode
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull(message = "from_currency should not be null")
    @Column(name = "from_currency", nullable = false)
    private Currency fromCurrency;

    @NotNull(message = "to_currency should not be null")
    @Column(name = "to_currency", nullable = false)
    private Currency toCurrency;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    @Column(name = "amount", precision = 21, scale = 2)
    private BigDecimal amount;
}
