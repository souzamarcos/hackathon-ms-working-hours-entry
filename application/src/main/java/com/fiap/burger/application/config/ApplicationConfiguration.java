package com.fiap.burger.application.config;

import com.fiap.burger.usecase.adapter.gateway.CustomerGateway;
import com.fiap.burger.usecase.adapter.gateway.OrderGateway;
import com.fiap.burger.usecase.adapter.gateway.ProductGateway;
import com.fiap.burger.usecase.adapter.usecase.OrderUseCase;
import com.fiap.burger.usecase.misc.token.TokenJwtUtils;
import com.fiap.burger.usecase.usecase.DefaultOrderUseCase;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public OrderUseCase orderUseCase(OrderGateway orderGateway, ProductGateway productGateway,
                                     CustomerGateway customerGateway, TokenJwtUtils tokenJwtUtils) {
        return new DefaultOrderUseCase(orderGateway, productGateway, customerGateway, tokenJwtUtils);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofMillis(3000))
            .setReadTimeout(Duration.ofMillis(3000))
            .build();
    }
}
