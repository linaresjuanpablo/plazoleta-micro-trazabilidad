package com.gastropolis.trazabilidad.application.mapper;

import com.gastropolis.trazabilidad.application.dto.response.OrderLogResponseDto;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T22:51:30-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IOrderLogResponseMapperImpl implements IOrderLogResponseMapper {

    @Override
    public OrderLogResponseDto toDto(OrderLogModel model) {
        if ( model == null ) {
            return null;
        }

        OrderLogResponseDto orderLogResponseDto = new OrderLogResponseDto();

        orderLogResponseDto.setId( model.getId() );
        orderLogResponseDto.setOrderId( model.getOrderId() );
        orderLogResponseDto.setClientId( model.getClientId() );
        orderLogResponseDto.setClientName( model.getClientName() );
        orderLogResponseDto.setEmployeeId( model.getEmployeeId() );
        orderLogResponseDto.setEmployeeName( model.getEmployeeName() );
        orderLogResponseDto.setPreviousStatus( model.getPreviousStatus() );
        orderLogResponseDto.setNewStatus( model.getNewStatus() );
        orderLogResponseDto.setTimestamp( model.getTimestamp() );

        return orderLogResponseDto;
    }

    @Override
    public List<OrderLogResponseDto> toDtoList(List<OrderLogModel> models) {
        if ( models == null ) {
            return null;
        }

        List<OrderLogResponseDto> list = new ArrayList<OrderLogResponseDto>( models.size() );
        for ( OrderLogModel orderLogModel : models ) {
            list.add( toDto( orderLogModel ) );
        }

        return list;
    }
}
