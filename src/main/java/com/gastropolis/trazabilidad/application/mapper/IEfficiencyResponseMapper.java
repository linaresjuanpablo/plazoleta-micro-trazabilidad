package com.gastropolis.trazabilidad.application.mapper;

import com.gastropolis.trazabilidad.application.dto.response.EfficiencyResultDto;
import com.gastropolis.trazabilidad.application.dto.response.EmployeeEfficiencyDto;
import com.gastropolis.trazabilidad.application.dto.response.OrderEfficiencyDto;
import com.gastropolis.trazabilidad.domain.model.EfficiencyResult;
import com.gastropolis.trazabilidad.domain.model.EmployeeEfficiencyModel;
import com.gastropolis.trazabilidad.domain.model.OrderEfficiencyModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEfficiencyResponseMapper {

    OrderEfficiencyDto toOrderEfficiencyDto(OrderEfficiencyModel model);

    List<OrderEfficiencyDto> toOrderEfficiencyDtoList(List<OrderEfficiencyModel> models);

    EmployeeEfficiencyDto toEmployeeEfficiencyDto(EmployeeEfficiencyModel model);

    List<EmployeeEfficiencyDto> toEmployeeEfficiencyDtoList(List<EmployeeEfficiencyModel> models);

    EfficiencyResultDto toEfficiencyResultDto(EfficiencyResult result);
}
