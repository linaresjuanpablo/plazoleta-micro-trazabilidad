package com.gastropolis.trazabilidad.application.handler.impl;

import com.gastropolis.trazabilidad.application.dto.request.CreateOrderLogRequestDto;
import com.gastropolis.trazabilidad.application.dto.response.EfficiencyResultDto;
import com.gastropolis.trazabilidad.application.dto.response.OrderLogResponseDto;
import com.gastropolis.trazabilidad.application.mapper.IEfficiencyResponseMapper;
import com.gastropolis.trazabilidad.application.mapper.IOrderLogRequestMapper;
import com.gastropolis.trazabilidad.application.mapper.IOrderLogResponseMapper;
import com.gastropolis.trazabilidad.domain.api.IOrderLogServicePort;
import com.gastropolis.trazabilidad.domain.model.EfficiencyResult;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLogHandlerTest {

    @Mock
    private IOrderLogServicePort orderLogServicePort;

    @Mock
    private IOrderLogRequestMapper orderLogRequestMapper;

    @Mock
    private IOrderLogResponseMapper orderLogResponseMapper;

    @Mock
    private IEfficiencyResponseMapper efficiencyResponseMapper;

    @InjectMocks
    private OrderLogHandler orderLogHandler;

    private static final String TOKEN = "Bearer test-token";
    private static final Long ORDER_ID = 1L;
    private static final Long CLIENT_ID = 10L;

    // -------------------------------------------------------------------------
    // createOrderLog
    // -------------------------------------------------------------------------

    @Test
    void createOrderLog_mapsRequestSavesAndReturnsDto() {
        CreateOrderLogRequestDto requestDto = new CreateOrderLogRequestDto();
        requestDto.setOrderId(ORDER_ID);
        requestDto.setClientId(CLIENT_ID);
        requestDto.setNewStatus("PENDIENTE");
        requestDto.setTimestamp(LocalDateTime.now());

        OrderLogModel model = new OrderLogModel();
        OrderLogModel saved = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, null, null, "PENDIENTE",
                LocalDateTime.now());
        OrderLogResponseDto responseDto = new OrderLogResponseDto();
        responseDto.setId(1L);
        responseDto.setNewStatus("PENDIENTE");

        when(orderLogRequestMapper.toModel(requestDto)).thenReturn(model);
        when(orderLogServicePort.saveOrderLog(model)).thenReturn(saved);
        when(orderLogResponseMapper.toDto(saved)).thenReturn(responseDto);

        OrderLogResponseDto result = orderLogHandler.createOrderLog(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDIENTE", result.getNewStatus());

        verify(orderLogRequestMapper).toModel(requestDto);
        verify(orderLogServicePort).saveOrderLog(model);
        verify(orderLogResponseMapper).toDto(saved);
    }

    // -------------------------------------------------------------------------
    // getOrderHistory
    // -------------------------------------------------------------------------

    @Test
    void getOrderHistory_delegatesAndMapsResult() {
        Long requestingUserId = CLIENT_ID;

        OrderLogModel log = new OrderLogModel(1L, ORDER_ID, CLIENT_ID, null, null, "PENDIENTE",
                LocalDateTime.now());
        List<OrderLogModel> logs = List.of(log);

        OrderLogResponseDto dto = new OrderLogResponseDto();
        dto.setId(1L);
        List<OrderLogResponseDto> dtoList = List.of(dto);

        when(orderLogServicePort.getOrderHistory(ORDER_ID, requestingUserId, TOKEN)).thenReturn(logs);
        when(orderLogResponseMapper.toDtoList(logs)).thenReturn(dtoList);

        List<OrderLogResponseDto> result = orderLogHandler.getOrderHistory(ORDER_ID, requestingUserId, TOKEN);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(orderLogServicePort).getOrderHistory(ORDER_ID, requestingUserId, TOKEN);
        verify(orderLogResponseMapper).toDtoList(logs);
    }

    @Test
    void getOrderHistory_returnsEmptyListWhenServiceReturnsEmpty() {
        when(orderLogServicePort.getOrderHistory(ORDER_ID, CLIENT_ID, TOKEN))
                .thenReturn(Collections.emptyList());
        when(orderLogResponseMapper.toDtoList(Collections.emptyList()))
                .thenReturn(Collections.emptyList());

        List<OrderLogResponseDto> result = orderLogHandler.getOrderHistory(ORDER_ID, CLIENT_ID, TOKEN);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // -------------------------------------------------------------------------
    // getEfficiency
    // -------------------------------------------------------------------------

    @Test
    void getEfficiency_delegatesAndMapsResult() {
        EfficiencyResult efficiencyResult = new EfficiencyResult(
                Collections.emptyList(), Collections.emptyList());
        EfficiencyResultDto efficiencyResultDto = new EfficiencyResultDto(
                Collections.emptyList(), Collections.emptyList());

        when(orderLogServicePort.getEfficiency(TOKEN)).thenReturn(efficiencyResult);
        when(efficiencyResponseMapper.toEfficiencyResultDto(efficiencyResult))
                .thenReturn(efficiencyResultDto);

        EfficiencyResultDto result = orderLogHandler.getEfficiency(TOKEN);

        assertNotNull(result);
        assertSame(efficiencyResultDto, result);

        verify(orderLogServicePort).getEfficiency(TOKEN);
        verify(efficiencyResponseMapper).toEfficiencyResultDto(efficiencyResult);
    }
}
