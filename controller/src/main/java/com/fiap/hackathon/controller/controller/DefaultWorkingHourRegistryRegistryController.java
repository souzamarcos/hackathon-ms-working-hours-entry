package com.fiap.hackathon.controller.controller;

import com.fiap.hackathon.controller.adapter.api.WorkingHourRegistryController;
import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.adapter.usecase.WorkingHourUseCase;
import org.springframework.stereotype.Component;

@Component
public class DefaultWorkingHourRegistryRegistryController implements WorkingHourRegistryController {

    private WorkingHourUseCase useCase;

    public DefaultWorkingHourRegistryRegistryController(WorkingHourUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public WorkingHourRegistry insert(WorkingHourRegistry registry) {
        return useCase.insert(registry);
    }
}

