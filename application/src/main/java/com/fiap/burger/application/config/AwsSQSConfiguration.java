package com.fiap.burger.application.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fiap.burger.usecase.misc.profiles.NotTest;
import com.fiap.burger.usecase.misc.profiles.Production;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;



@EnableSqs
@NotTest
@Configuration
public class AwsSQSConfiguration
{
    @Value("${localstack.url}")
    private String LOCALSTACK_ENDPOINT;

    @Value("${cloud.aws.region}")
    private String awsRegion;

    @Bean
    @Primary
    @Production
    public AmazonSQS productionAmazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .build();
    }

    @Bean
    public AmazonSQS defaultAmazonSQSAsync() {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(LOCALSTACK_ENDPOINT, awsRegion);

        return AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQS amazonSQS) {
        return new QueueMessagingTemplate((AmazonSQSAsync) amazonSQS);
    }
}
