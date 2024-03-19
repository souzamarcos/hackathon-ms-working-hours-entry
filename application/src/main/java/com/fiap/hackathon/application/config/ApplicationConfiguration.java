package com.fiap.hackathon.application.config;

import com.fiap.hackathon.usecase.adapter.messager.WorkingHourEntryMessenger;
import com.fiap.hackathon.usecase.usecase.DefaultWorkingHourUseCase;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class ApplicationConfiguration {
    @Bean
    public DefaultWorkingHourUseCase DefaultWorkingHourUseCase(WorkingHourEntryMessenger workingHourEntryMessenger) {
        return new DefaultWorkingHourUseCase(workingHourEntryMessenger);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofMillis(3000))
            .setReadTimeout(Duration.ofMillis(3000))
            .build();
    }
}
