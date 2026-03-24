package com.gastropolis.trazabilidad.application.mapper;

import com.gastropolis.trazabilidad.application.dto.response.OrderLogResponseDto;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderLogResponseMapper {

    OrderLogResponseDto toDto(OrderLogModel model);

    List<OrderLogResponseDto> toDtoList(List<OrderLogModel> models);
}
