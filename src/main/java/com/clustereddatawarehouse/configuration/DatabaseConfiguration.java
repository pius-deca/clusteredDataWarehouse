package com.clustereddatawarehouse.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("com.clustereddatawarehouse.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {}
