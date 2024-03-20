package com.fiap.hackathon.api.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.hackathon.api.dto.workinghour.response.WorkingHourRegistryResponseDto;
import com.fiap.hackathon.controller.adapter.api.WorkingHourRegistryController;
import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.api.misc.token.TokenJwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequestMapping("/working-hour-entry")
@Tag(name = "working-hour-entry", description = "API responsável pela entrada dos registros de ponto dos funcionários.")
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class WorkingHourEntryApi {

    private TokenJwtUtils tokenJwtUtils;
    private WorkingHourRegistryController controller;

    public WorkingHourEntryApi(
        TokenJwtUtils tokenJwtUtils,
        WorkingHourRegistryController controller
    ) {
        this.tokenJwtUtils = tokenJwtUtils;
        this.controller = controller;
    }

    @Operation(summary = "Bater ponto", description = "Inserir registro de ponto", tags = {"working-hour-entry"})
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Request inválida")})
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping
    public WorkingHourRegistryResponseDto insert() {
        var employeeId = getEployeeId();
        var registry = new WorkingHourRegistry(employeeId, Instant.now());
        return WorkingHourRegistryResponseDto.toResponseDto(controller.insert(registry));
    }

    private String getEployeeId() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}
