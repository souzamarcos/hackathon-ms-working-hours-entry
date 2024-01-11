package com.fiap.burger.application.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fiap.burger.usecase.adapter.gateway.OrderGateway;
import com.fiap.burger.usecase.adapter.gateway.ProductGateway;
import com.fiap.burger.usecase.adapter.usecase.OrderUseCase;
import com.fiap.burger.usecase.usecase.DefaultOrderUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@EnableSqs
public class ApplicationConfiguration {
    @Value("${cloud.aws.region}")
    private String awsRegion;
    @Value("${cloud.aws.end-point.uri}")
    private String awsEndpointUri;

    @Bean
    public OrderUseCase orderUseCase(OrderGateway orderGateway, ProductGateway productGateway) {
        return new DefaultOrderUseCase(orderGateway, productGateway);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofMillis(3000))
            .setReadTimeout(Duration.ofMillis(3000))
            .build();
    }
    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(awsEndpointUri, awsRegion);

        return AmazonSQSAsyncClientBuilder
            .standard()
            .withEndpointConfiguration(endpoint)
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }
}
