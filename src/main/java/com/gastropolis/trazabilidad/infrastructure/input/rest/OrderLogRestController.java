package com.gastropolis.trazabilidad.infrastructure.input.rest;

import com.gastropolis.trazabilidad.application.dto.request.CreateOrderLogRequestDto;
import com.gastropolis.trazabilidad.application.dto.response.EfficiencyResultDto;
import com.gastropolis.trazabilidad.application.dto.response.OrderLogResponseDto;
import com.gastropolis.trazabilidad.application.handler.IOrderLogHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/traceability")
@RequiredArgsConstructor
@Tag(name = "Trazabilidad", description = "API para gestión de trazabilidad de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class OrderLogRestController {

    private final IOrderLogHandler orderLogHandler;

    @PostMapping("/")
    @Operation(
            summary = "Registrar cambio de estado de pedido (HU17)",
            description = "Crea un nuevo log de trazabilidad para un pedido. Uso interno desde MS-Plazoleta."
    )
    public ResponseEntity<OrderLogResponseDto> createOrderLog(
            @Valid @RequestBody CreateOrderLogRequestDto requestDto) {
        OrderLogResponseDto response = orderLogHandler.createOrderLog(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/order/{orderId}")
    @Operation(
            summary = "Ver historial de un pedido (HU17)",
            description = "Retorna el historial de cambios de estado de un pedido. " +
                          "Solo accesible por el cliente dueño del pedido."
    )
    public ResponseEntity<List<OrderLogResponseDto>> getOrderHistory(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String token,
            Principal principal) {
        Long requestingUserId = extractUserId(principal);
        List<OrderLogResponseDto> history = orderLogHandler.getOrderHistory(orderId, requestingUserId, token);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/efficiency")
    @PreAuthorize("hasAuthority('PROPIETARIO')")
    @Operation(
            summary = "Ver eficiencia de empleados (HU18)",
            description = "Retorna estadísticas de eficiencia: tiempo por pedido y ranking de empleados. " +
                          "Solo para PROPIETARIO."
    )
    public ResponseEntity<EfficiencyResultDto> getEfficiency(
            @RequestHeader("Authorization") String token) {
        EfficiencyResultDto efficiency = orderLogHandler.getEfficiency(token);
        return ResponseEntity.ok(efficiency);
    }

    private Long extractUserId(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken authToken) {
            Object credentials = authToken.getCredentials();
            if (credentials instanceof Long) {
                return (Long) credentials;
            }
        }
        return null;
    }
}
