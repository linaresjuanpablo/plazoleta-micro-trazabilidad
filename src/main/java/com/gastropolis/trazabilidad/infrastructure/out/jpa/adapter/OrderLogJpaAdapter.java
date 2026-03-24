package com.gastropolis.trazabilidad.infrastructure.out.jpa.adapter;

import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import com.gastropolis.trazabilidad.domain.spi.IOrderLogPersistencePort;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.entity.OrderLogEntity;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.mapper.IOrderLogEntityMapper;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.repository.IOrderLogRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderLogJpaAdapter implements IOrderLogPersistencePort {

    private final IOrderLogRepository orderLogRepository;
    private final IOrderLogEntityMapper orderLogEntityMapper;

    @Override
    public OrderLogModel save(OrderLogModel orderLogModel) {
        OrderLogEntity entity = orderLogEntityMapper.toEntity(orderLogModel);
        OrderLogEntity saved = orderLogRepository.save(entity);
        return orderLogEntityMapper.toModel(saved);
    }

    @Override
    public List<OrderLogModel> findByOrderId(Long orderId) {
        List<OrderLogEntity> entities = orderLogRepository.findByOrderId(orderId);
        return orderLogEntityMapper.toModelList(entities);
    }

    @Override
    public List<OrderLogModel> findAll() {
        List<OrderLogEntity> entities = orderLogRepository.findAll();
        return orderLogEntityMapper.toModelList(entities);
    }
}
