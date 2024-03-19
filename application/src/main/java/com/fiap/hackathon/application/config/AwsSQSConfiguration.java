package com.fiap.hackathon.application.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fiap.hackathon.usecase.misc.profiles.NotProduction;
import com.fiap.hackathon.usecase.misc.profiles.NotTest;
import com.fiap.hackathon.usecase.misc.profiles.Production;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;



@NotTest
@EnableSqs
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
    public AmazonSQSAsync productionAmazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder
            .standard()
            .withCredentials(new DefaultAWSCredentialsProviderChain())
            .build();
    }

    @Bean
    @Primary
    @NotProduction
    public AmazonSQSAsync defaultAmazonSQSAsync() {
        AwsClientBuilder.EndpointConfiguration endpoint = new AwsClientBuilder.EndpointConfiguration(LOCALSTACK_ENDPOINT, awsRegion);

        return AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(endpoint)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("fiap", "fiap")))
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }
}
