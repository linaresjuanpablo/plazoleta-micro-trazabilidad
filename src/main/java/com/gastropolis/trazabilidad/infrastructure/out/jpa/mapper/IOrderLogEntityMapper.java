package com.gastropolis.trazabilidad.infrastructure.out.jpa.mapper;

import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import com.gastropolis.trazabilidad.infrastructure.out.jpa.entity.OrderLogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderLogEntityMapper {

    OrderLogEntity toEntity(OrderLogModel model);

    OrderLogModel toModel(OrderLogEntity entity);

    List<OrderLogModel> toModelList(List<OrderLogEntity> entities);
}
