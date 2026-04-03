package com.gastropolis.trazabilidad.application.mapper;

import com.gastropolis.trazabilidad.application.dto.request.CreateOrderLogRequestDto;
import com.gastropolis.trazabilidad.domain.model.OrderLogModel;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-30T22:51:29-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.2.jar, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class IOrderLogRequestMapperImpl implements IOrderLogRequestMapper {

    @Override
    public OrderLogModel toModel(CreateOrderLogRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        OrderLogModel orderLogModel = new OrderLogModel();

        orderLogModel.setOrderId( dto.getOrderId() );
        orderLogModel.setClientId( dto.getClientId() );
        orderLogModel.setEmployeeId( dto.getEmployeeId() );
        orderLogModel.setPreviousStatus( dto.getPreviousStatus() );
        orderLogModel.setNewStatus( dto.getNewStatus() );
        orderLogModel.setTimestamp( dto.getTimestamp() );

        return orderLogModel;
    }
}
