package com.fiap.burger.api.api;

import com.fiap.burger.api.dto.order.request.OrderInsertRequestDto;
import com.fiap.burger.api.dto.order.request.OrderUpdateStatusRequestDto;
import com.fiap.burger.api.dto.order.response.ResumedOrderResponseDto;
import com.fiap.burger.api.dto.order.response.OrderResponseDto;
import com.fiap.burger.controller.adapter.api.OrderController;
import com.fiap.burger.entity.order.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "pedido", description = "API responsável pelo controle de pedidos.")
public class OrderApi {

    @Autowired
    private OrderController controller;

    @Operation(summary = "Criar pedido", description = "Criação de um novo pedido", tags = {"pedido"})
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Pedido inválido")})
    @PostMapping()
    public ResumedOrderResponseDto insert(@RequestBody OrderInsertRequestDto orderDto) {
        return ResumedOrderResponseDto.toResponseDto(controller.insert(orderDto.toEntity()));
    }

    @Operation(summary = "Consultar pedido", description = "Consultar pedidos", tags = {"pedido"})
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Request inválida")})
    @GetMapping("/{orderId}")
    public OrderResponseDto findById(@PathVariable Long orderId) {
        return OrderResponseDto.toResponseDto(controller.findById(orderId));
    }

    @Operation(summary = "Atualizar status do pedido", description = "Atualizar status de um pedido", tags = {"pedido"})
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Requisição inválida")})
    @PostMapping("/{orderId}/status")
    public ResumedOrderResponseDto updateStatus(@PathVariable Long orderId, @RequestBody OrderUpdateStatusRequestDto orderDto) {
        return ResumedOrderResponseDto.toResponseDto(controller.updateStatus(orderId, orderDto.newStatus()));
    }

    @Operation(summary = "Listar todos os pedidos", description = "Listar todos os pedidos", tags = {"pedido"})
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Request inválida")})
    @GetMapping()
    public List<ResumedOrderResponseDto> findAll(@RequestParam @Nullable OrderStatus status) {
        return controller.findAllBy(status).stream().map(ResumedOrderResponseDto::toResponseDto).toList();
    }

    @Operation(summary = "Listar pedidos em progresso", description = "Listar pedidos em progresso. Status igual a PRONTO, EM_PREPARAÇÂO ou RECEBIDO.", tags = {"pedido"})
    @ApiResponses(value = {@ApiResponse(responseCode = "400", description = "Request inválida")})
    @GetMapping("/in-progress")
    public List<ResumedOrderResponseDto> findAllInProgress() {
        return controller.findAllInProgress().stream().map(ResumedOrderResponseDto::toResponseDto).toList();
    }
}
