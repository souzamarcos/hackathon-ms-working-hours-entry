package com.fiap.hackathon.api.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "health", description = "API auxiliar para verificar saúde da aplicação")
public class HealthApi {
    @GetMapping("/health")
    @Operation(summary = "Verificar saúde da aplicação", description = "Verificar se a aplicação está rodando", tags = {"health"})
    public String index() {
        return "Application is running!";
    }
}
