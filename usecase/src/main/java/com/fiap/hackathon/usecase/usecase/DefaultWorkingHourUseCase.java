package com.fiap.hackathon.usecase.usecase;

import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.adapter.messager.WorkingHourEntryMessenger;
import com.fiap.hackathon.usecase.adapter.usecase.WorkingHourUseCase;

public class DefaultWorkingHourUseCase implements WorkingHourUseCase {

    private final WorkingHourEntryMessenger workingHourEntryMessenger;

    public DefaultWorkingHourUseCase(WorkingHourEntryMessenger workingHourEntryMessenger) {
        this.workingHourEntryMessenger = workingHourEntryMessenger;
    }

    @Override
    public WorkingHourRegistry insert(WorkingHourRegistry registry) {
        workingHourEntryMessenger.sendMessage(registry);
        return registry;
    }
}
