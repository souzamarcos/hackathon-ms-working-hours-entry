package com.fiap.hackathon.api.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.hackathon.api.dto.order.response.WorkingHourRegistryResponseDto;
import com.fiap.hackathon.controller.adapter.api.WorkingHourRegistryController;
import com.fiap.hackathon.entity.WorkingHourRegistry;
import com.fiap.hackathon.usecase.misc.exception.InvalidAttributeException;
import com.fiap.hackathon.usecase.misc.exception.TokenJwtException;
import com.fiap.hackathon.usecase.misc.token.TokenJwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/working-hour-entry")
@Tag(name = "working-hour-entry", description = "API responsável pela entrada dos registros de ponto dos funcionários.")
public class WorkingHourEntryApi {

    @Autowired
    private TokenJwtUtils tokenJwtUtils;
    @Autowired
    private WorkingHourRegistryController controller;

    public WorkingHourEntryApi(
        @Autowired TokenJwtUtils tokenJwtUtils,
        @Autowired WorkingHourRegistryController controller
    ) {
        this.tokenJwtUtils = tokenJwtUtils;
        this.controller = controller;
    }

    @Operation(summary = "Bater ponto", description = "Inserir registro de ponto", tags = {"working-hour-entry"})
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Request inválida")})
    @PostMapping()
    public WorkingHourRegistryResponseDto insert(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        var token = validateAndExtractToken(authorizationHeader);
        var employeeId = extractIdFromToken(token);
        var registry = new WorkingHourRegistry(employeeId, LocalDateTime.now());
        return WorkingHourRegistryResponseDto.toResponseDto(controller.insert(registry));
    }

    private String validateAndExtractToken(String headerAuthorizationValue) {
        if (!headerAuthorizationValue.startsWith("Bearer ")) {
            throw new InvalidAttributeException("Valor do header inválido", HttpHeaders.AUTHORIZATION);
        }

        return headerAuthorizationValue.substring(7);
    }

    protected String extractIdFromToken(String token) {
        DecodedJWT decodedJwt = tokenJwtUtils.readToken(token);
        return Optional.ofNullable(decodedJwt.getClaim("id").asString()).orElseThrow(() -> new TokenJwtException("Malformed token."));
    }
}
