package com.fiap.hackathon.usecase.adapter.messager;

import com.fiap.hackathon.entity.WorkingHourRegistry;

public interface WorkingHourEntryMessenger {
    void sendMessage(WorkingHourRegistry registry);
}
