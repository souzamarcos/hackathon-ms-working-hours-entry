package com.fiap.burger.application.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@EnableSqs
@Configuration
public class AwsSQSConfiguration
{
    @Value("${cloud.aws.region}")
    private String awsRegion;
    @Value("${cloud.aws.end-point.uri}")
    private String awsEndpointUri;

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
