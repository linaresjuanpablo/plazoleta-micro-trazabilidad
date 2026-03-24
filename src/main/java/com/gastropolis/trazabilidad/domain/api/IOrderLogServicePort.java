package com.gastropolis.trazabilidad.domain.api;

import com.gastropolis.trazabilidad.domain.model.EfficiencyResult;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;

import java.util.List;

public interface IOrderLogServicePort {

    OrderLogModel saveOrderLog(OrderLogModel orderLogModel);

    List<OrderLogModel> getOrderHistory(Long orderId, Long requestingUserId, String token);

    EfficiencyResult getEfficiency(String token);
}
