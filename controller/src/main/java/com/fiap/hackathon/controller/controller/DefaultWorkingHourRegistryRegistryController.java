package com.fiap.hackathon.controller.controller;

import com.fiap.hackathon.controller.adapter.api.WorkingHourRegistryController;
import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.adapter.usecase.WorkingHourUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultWorkingHourRegistryRegistryController implements WorkingHourRegistryController {
    @Autowired
    private WorkingHourUseCase useCase;

    @Override
    public WorkingHourRegistry insert(WorkingHourRegistry registry) {
        return useCase.insert(registry);
    }
}

