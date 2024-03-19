package com.fiap.hackathon.usecase.adapter.gateway;

import com.fiap.hackathon.entity.WorkingHourRegistry;

public interface WorkingHourGateway {
    WorkingHourRegistry insert(WorkingHourRegistry registry);
}
