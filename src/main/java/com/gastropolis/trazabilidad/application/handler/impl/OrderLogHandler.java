package com.gastropolis.trazabilidad.application.handler.impl;

import com.gastropolis.trazabilidad.application.dto.request.CreateOrderLogRequestDto;
import com.gastropolis.trazabilidad.application.dto.response.EfficiencyResultDto;
import com.gastropolis.trazabilidad.application.dto.response.OrderLogResponseDto;
import com.gastropolis.trazabilidad.application.handler.IOrderLogHandler;
import com.gastropolis.trazabilidad.application.mapper.IEfficiencyResponseMapper;
import com.gastropolis.trazabilidad.application.mapper.IOrderLogRequestMapper;
import com.gastropolis.trazabilidad.application.mapper.IOrderLogResponseMapper;
import com.gastropolis.trazabilidad.domain.api.IOrderLogServicePort;
import com.gastropolis.trazabilidad.domain.model.EfficiencyResult;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderLogHandler implements IOrderLogHandler {

    private final IOrderLogServicePort orderLogServicePort;
    private final IOrderLogRequestMapper orderLogRequestMapper;
    private final IOrderLogResponseMapper orderLogResponseMapper;
    private final IEfficiencyResponseMapper efficiencyResponseMapper;

    @Override
    public OrderLogResponseDto createOrderLog(CreateOrderLogRequestDto requestDto) {
        OrderLogModel model = orderLogRequestMapper.toModel(requestDto);
        OrderLogModel saved = orderLogServicePort.saveOrderLog(model);
        return orderLogResponseMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderLogResponseDto> getOrderHistory(Long orderId, Long requestingUserId, String token) {
        List<OrderLogModel> logs = orderLogServicePort.getOrderHistory(orderId, requestingUserId, token);
        return orderLogResponseMapper.toDtoList(logs);
    }

    @Override
    @Transactional(readOnly = true)
    public EfficiencyResultDto getEfficiency(String token) {
        EfficiencyResult result = orderLogServicePort.getEfficiency(token);
        return efficiencyResponseMapper.toEfficiencyResultDto(result);
    }
}
