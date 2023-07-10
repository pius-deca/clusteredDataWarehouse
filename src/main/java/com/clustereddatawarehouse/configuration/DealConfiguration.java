package com.clustereddatawarehouse.configuration;

import com.clustereddatawarehouse.repository.DealRepository;
import com.clustereddatawarehouse.service.validator.DealValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DealConfiguration {

    @Bean
    public DealValidator dealValidator(DealRepository dealRepository) {
        return new DealValidator(dealRepository);
    }
}
