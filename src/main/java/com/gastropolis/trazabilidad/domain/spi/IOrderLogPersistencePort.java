package com.gastropolis.trazabilidad.domain.spi;

import com.gastropolis.trazabilidad.domain.model.OrderLogModel;

import java.util.List;

public interface IOrderLogPersistencePort {

    OrderLogModel save(OrderLogModel orderLogModel);

    List<OrderLogModel> findByOrderId(Long orderId);

    List<OrderLogModel> findAll();
}
