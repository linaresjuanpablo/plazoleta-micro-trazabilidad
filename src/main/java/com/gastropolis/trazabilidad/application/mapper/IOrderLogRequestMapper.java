package com.gastropolis.trazabilidad.application.mapper;

import com.gastropolis.trazabilidad.application.dto.request.CreateOrderLogRequestDto;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderLogRequestMapper {

    OrderLogModel toModel(CreateOrderLogRequestDto dto);
}
