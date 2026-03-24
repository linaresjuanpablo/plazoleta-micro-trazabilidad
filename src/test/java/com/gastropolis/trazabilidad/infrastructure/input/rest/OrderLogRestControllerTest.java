package com.gastropolis.trazabilidad.infrastructure.input.rest;

import com.gastropolis.trazabilidad.application.dto.request.CreateOrderLogRequestDto;
import com.gastropolis.trazabilidad.application.dto.response.EfficiencyResultDto;
import com.gastropolis.trazabilidad.application.dto.response.OrderLogResponseDto;
import com.gastropolis.trazabilidad.application.handler.IOrderLogHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLogRestControllerTest {

    @Mock
    private IOrderLogHandler orderLogHandler;

    @InjectMocks
    private OrderLogRestController controller;

    @Test
    void createOrderLog_returnsCreatedStatus() {
        CreateOrderLogRequestDto request = new CreateOrderLogRequestDto();
        request.setOrderId(1L);
        request.setClientId(5L);
        request.setNewStatus("EN_PREPARACION");
        request.setTimestamp(LocalDateTime.now());

        OrderLogResponseDto response = new OrderLogResponseDto();
        response.setId(1L);
        when(orderLogHandler.createOrderLog(request)).thenReturn(response);

        ResponseEntity<OrderLogResponseDto> result = controller.createOrderLog(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(orderLogHandler).createOrderLog(request);
    }

    @Test
    void getOrderHistory_withLongCredentials_extractsUserIdCorrectly() {
        Long orderId = 1L;
        Long userId = 5L;
        String token = "Bearer token123";
        List<OrderLogResponseDto> history = List.of(new OrderLogResponseDto());

        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("user@test.com", userId, List.of());
        when(orderLogHandler.getOrderHistory(orderId, userId, token)).thenReturn(history);

        ResponseEntity<List<OrderLogResponseDto>> result = controller.getOrderHistory(orderId, token, principal);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(orderLogHandler).getOrderHistory(orderId, userId, token);
    }

    @Test
    void getOrderHistory_withNonLongCredentials_returnsNullUserId() {
        Long orderId = 1L;
        String token = "Bearer token123";
        List<OrderLogResponseDto> history = List.of();

        UsernamePasswordAuthenticationToken principal =
                new UsernamePasswordAuthenticationToken("user@test.com", "string-credentials", List.of());
        when(orderLogHandler.getOrderHistory(orderId, null, token)).thenReturn(history);

        ResponseEntity<List<OrderLogResponseDto>> result = controller.getOrderHistory(orderId, token, principal);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(orderLogHandler).getOrderHistory(orderId, null, token);
    }

    @Test
    void getOrderHistory_withNullPrincipal_returnsNullUserId() {
        Long orderId = 1L;
        String token = "Bearer token123";
        List<OrderLogResponseDto> history = List.of();

        when(orderLogHandler.getOrderHistory(orderId, null, token)).thenReturn(history);

        ResponseEntity<List<OrderLogResponseDto>> result = controller.getOrderHistory(orderId, token, null);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(orderLogHandler).getOrderHistory(orderId, null, token);
    }

    @Test
    void getEfficiency_returnsEfficiencyData() {
        String token = "Bearer token123";
        EfficiencyResultDto efficiency = new EfficiencyResultDto();
        when(orderLogHandler.getEfficiency(token)).thenReturn(efficiency);

        ResponseEntity<EfficiencyResultDto> result = controller.getEfficiency(token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(efficiency, result.getBody());
        verify(orderLogHandler).getEfficiency(token);
    }
}
