package com.gastropolis.trazabilidad.application.mapper;

import com.gastropolis.trazabilidad.application.dto.response.EfficiencyResultDto;
import com.gastropolis.trazabilidad.application.dto.response.EmployeeEfficiencyDto;
import com.gastropolis.trazabilidad.application.dto.response.OrderEfficiencyDto;
import com.gastropolis.trazabilidad.domain.model.EfficiencyResult;
import com.gastropolis.trazabilidad.domain.model.EmployeeEfficiencyModel;
import com.gastropolis.trazabilidad.domain.model.OrderEfficiencyModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T22:51:31-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IEfficiencyResponseMapperImpl implements IEfficiencyResponseMapper {

    @Override
    public OrderEfficiencyDto toOrderEfficiencyDto(OrderEfficiencyModel model) {
        if ( model == null ) {
            return null;
        }

        OrderEfficiencyDto orderEfficiencyDto = new OrderEfficiencyDto();

        orderEfficiencyDto.setOrderId( model.getOrderId() );
        orderEfficiencyDto.setStartTime( model.getStartTime() );
        orderEfficiencyDto.setEndTime( model.getEndTime() );
        orderEfficiencyDto.setDurationMinutes( model.getDurationMinutes() );

        return orderEfficiencyDto;
    }

    @Override
    public List<OrderEfficiencyDto> toOrderEfficiencyDtoList(List<OrderEfficiencyModel> models) {
        if ( models == null ) {
            return null;
        }

        List<OrderEfficiencyDto> list = new ArrayList<OrderEfficiencyDto>( models.size() );
        for ( OrderEfficiencyModel orderEfficiencyModel : models ) {
            list.add( toOrderEfficiencyDto( orderEfficiencyModel ) );
        }

        return list;
    }

    @Override
    public EmployeeEfficiencyDto toEmployeeEfficiencyDto(EmployeeEfficiencyModel model) {
        if ( model == null ) {
            return null;
        }

        EmployeeEfficiencyDto employeeEfficiencyDto = new EmployeeEfficiencyDto();

        employeeEfficiencyDto.setEmployeeId( model.getEmployeeId() );
        employeeEfficiencyDto.setEmployeeName( model.getEmployeeName() );
        employeeEfficiencyDto.setAverageDurationMinutes( model.getAverageDurationMinutes() );
        employeeEfficiencyDto.setTotalOrders( model.getTotalOrders() );

        return employeeEfficiencyDto;
    }

    @Override
    public List<EmployeeEfficiencyDto> toEmployeeEfficiencyDtoList(List<EmployeeEfficiencyModel> models) {
        if ( models == null ) {
            return null;
        }

        List<EmployeeEfficiencyDto> list = new ArrayList<EmployeeEfficiencyDto>( models.size() );
        for ( EmployeeEfficiencyModel employeeEfficiencyModel : models ) {
            list.add( toEmployeeEfficiencyDto( employeeEfficiencyModel ) );
        }

        return list;
    }

    @Override
    public EfficiencyResultDto toEfficiencyResultDto(EfficiencyResult result) {
        if ( result == null ) {
            return null;
        }

        EfficiencyResultDto efficiencyResultDto = new EfficiencyResultDto();

        efficiencyResultDto.setOrders( toOrderEfficiencyDtoList( result.getOrders() ) );
        efficiencyResultDto.setEmployeeRanking( toEmployeeEfficiencyDtoList( result.getEmployeeRanking() ) );

        return efficiencyResultDto;
    }
}
