package com.gastropolis.trazabilidad.application.handler;

import com.gastropolis.trazabilidad.application.dto.request.CreateOrderLogRequestDto;
import com.gastropolis.trazabilidad.application.dto.response.EfficiencyResultDto;
import com.gastropolis.trazabilidad.application.dto.response.OrderLogResponseDto;

import java.util.List;

public interface IOrderLogHandler {

    OrderLogResponseDto createOrderLog(CreateOrderLogRequestDto requestDto);

    List<OrderLogResponseDto> getOrderHistory(Long orderId, Long requestingUserId, String token);

    EfficiencyResultDto getEfficiency(String token);
}
